package com.mbds.vamp.dashboardapp.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;


public class ItineraireTask extends AsyncTask<Void, Integer, Boolean> {

    /*******************************************************/
    /** CONSTANTES.
     /*******************************************************/
    private static final String TOAST_MSG = "Calcul de l'itinéraire en cours";
    private static final String TOAST_ERR_MAJ = "Impossible de trouver un itinéraire";

    /*******************************************************/
    /** ATTRIBUTS.
     /*******************************************************/
    private Context context;
    private GoogleMap gMap;
    private LatLng editDepart;
    private LatLng editArrivee;
    private final ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();
    private ArrayList<LatLng> lstLatLngPOI = new ArrayList<LatLng>();
    private String editDeparts ;
    private String editArrivees;

    /*******************************************************/
    /** METHODES / FONCTIONS.
     /*******************************************************/
    /**
     * Constructeur.
     * @param context
     * @param gMap
     * @param editDepart
     * @param editArrivee
     */

    public ItineraireTask(final Context context, final GoogleMap gMap, final LatLng editDepart, final LatLng editArrivee,final ArrayList<LatLng> lstLatLngPOI) {
        this.context = context;
        this.gMap= gMap;
        this.editDepart = editDepart;
        this.editArrivee = editArrivee;
        this.lstLatLngPOI = lstLatLngPOI;

        Geocoder geocoder;
        List<Address> addressesD;
        List<Address> addressesA;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addressesD = geocoder.getFromLocation(this.editDepart.latitude, this.editDepart.longitude, 1);
            addressesA = geocoder.getFromLocation(this.editArrivee.latitude, this.editArrivee.longitude, 1);
            this.editDeparts = addressesD.get(0).getLocality();
            this.editArrivees = addressesA.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreExecute() {
        Toast.makeText(context, TOAST_MSG, Toast.LENGTH_LONG).show();
    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?sensor=false&language=fr");
        url.append("&origin=");
        url.append(this.editDeparts.replace(' ', '+'));
        url.append("&destination=");
        url.append(this.editArrivees.replace(' ', '+'));

        //APPEL DU WEBSERVICE
        try {
            final InputStream stream = new URL(url.toString()).openStream();
            //TRAITEMENT DES DONNEES
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringComments(true);

            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            final Document document = documentBuilder.parse(stream);
            document.getDocumentElement().normalize();

            //RECUPERATION DU STATUS DES REQUETES
            final String status = document.getElementsByTagName("status").item(0).getTextContent();
            if(!"OK".equals(status)) {
                return false;
            }

            //RECUPERATION DES ETAPE DE L'ITINERAIRE
            final Element elementLeg = (Element) document.getElementsByTagName("leg").item(0);
            final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
            final int length = nodeListStep.getLength();

            for(int i=0; i<length; i++) {
                final Node nodeStep = nodeListStep.item(i);

                if(nodeStep.getNodeType() == Node.ELEMENT_NODE) {
                    final Element elementStep = (Element) nodeStep;

                    //DECODER LES POINTS DU XML
                    decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());
                }
            }

            return true;
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        return  null;
    }
    /**
     * METHODE QUI DECODE LES POINTS EN LATITUDES ET LONGITUDES
     */
    private void decodePolylines(final String encodedPoints) {
        int index = 0;
        int lat = 0, lng = 0;

        while (index < encodedPoints.length()) {
            int b, shift = 0, result = 0;

            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;

            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            lstLatLng.add(new LatLng((double)lat/1E5, (double)lng/1E5));
        }
    }
    @Override
    protected void onPostExecute(final Boolean result) {
        if(!result) {
            Toast.makeText(context, TOAST_ERR_MAJ, Toast.LENGTH_SHORT).show();
        }
        else {
            //DECLARER LE POLYLINE, CAD LE TRAIT (BLEU DANS NOTRE CAS) QUE L'ON AJOUTE SUR LA CARTE POUR TRACER L'ITINERAIRE
            final PolylineOptions polylines = new PolylineOptions();
            polylines.color(Color.BLUE);

            //CONSTRUIRE LE POLYLINE
            for(final LatLng latLng : lstLatLng) {
                polylines.add(latLng);
            }

            //MARKER VERT POUR INDIQUER LE POINT DE DEPART
            final MarkerOptions markerA = new MarkerOptions();
            markerA.position(lstLatLng.get(0)).title(this.editDeparts+", France ").snippet("Latitude : "+editDepart.latitude +"| Longitude : "+editDepart.longitude);
            markerA.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            //MARKER ROUGE POUR INDIQUER LE POINT D'ARRIVER
            final MarkerOptions markerB = new MarkerOptions();
            markerB.position(lstLatLng.get(lstLatLng.size()-1)).title(this.editArrivees+", France ").snippet("Latitude : "+editArrivee.latitude +"| Longitude : "+editArrivee.longitude);
            markerB.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            //MARKERS DE POI
            final ArrayList<MarkerOptions> markerPOI = new ArrayList<>();
            if(!lstLatLngPOI.isEmpty()){
               for(int i = 0; i < lstLatLngPOI.size();i++){
                   MarkerOptions opt = new MarkerOptions().position(new LatLng(lstLatLngPOI.get(i).latitude, lstLatLngPOI.get(i).longitude)).title(this.editDeparts+", France ").snippet("Latitude : "+lstLatLngPOI.get(i).latitude +"| Longitude : "+lstLatLngPOI.get(i).longitude);
                   opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                   markerPOI.add(opt);
                   gMap.addMarker(markerPOI.get(i));
               }

            }

            //MISE A JOUR DE LA CARTE
            float zoomLevel = 12.0f;
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lstLatLng.get(0), zoomLevel));
            gMap.addMarker(markerA);
            gMap.addPolyline(polylines);
            gMap.addMarker(markerB);
        }
    }
}
