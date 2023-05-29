import { AfterViewInit, Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { GoogleMap, MapInfoWindow, MapMarker } from '@angular/google-maps';
import { Toilet } from 'src/app/models/toilet';

import { ToiletService } from 'src/app/services/toilet.service';

@Component({
  selector: 'app-googlemap',
  templateUrl: './googlemap.component.html',
  styleUrls: ['./googlemap.component.css']
})
export class GooglemapComponent implements OnInit, AfterViewInit {
  
  // Toilets Data
  toilets: Toilet[] = []

  // GoogleMapComponent Settings
  center!: google.maps.LatLngLiteral;
  zoom!: number;
  options: google.maps.MapOptions = {
    disableDefaultUI: true,
    fullscreenControl: true,
    zoomControl: true,
    streetViewControl: true
  };

  // Settings for Current Position Marker
  // currentMapMarkerLabel = { color: 'blue', text: 'Text' };
  // currentMapMarkerOptions: google.maps.MarkerOptions = { }
  
  
  // Settings for Toilet Markers
  markerPositions: any[] = [];
  markerOptions: google.maps.MarkerOptions = {
    icon: {
      url: "/assets/images/toiletmarker.png",
      labelOrigin: new google.maps.Point(65, 0)
    }
      
  };

  // Settings for info window
  info!: Toilet

  @ViewChild(GoogleMap) public map!: GoogleMap;

  @ViewChild(MapInfoWindow) infoWindow: MapInfoWindow | undefined;

  @ViewChild('search')
  public searchElementRef!: ElementRef;

  constructor(private toiletService: ToiletService, private ngZone: NgZone) {}

  ngOnInit(): void {

    // Get current location. Need to add marker
    navigator.geolocation.getCurrentPosition((position) => {

      this.center = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      }

      this.zoom = 16;

      // TODO: Need to change to retrieve close toilets
      this.loadToilets();

    });

  }

  private loadToilets() {
    this.toiletService.getAllToilets()
      .then(result => {
        this.markerPositions = result.map(toilet => {
          const toiletPosition = {
            lat: Number(toilet['latitude']),
            lng: Number(toilet['longitude'])
          };
          toilet.distanceFromCurrentPosition = this.calculateDistanceInKm(this.center, toiletPosition);
          const markerPosition = {
            position: toiletPosition,
            label: {
              color: 'red',
              text: toilet.count ? `${toilet.distanceFromCurrentPosition}km, Rating: ${toilet.average}(${toilet.count})` : `${toilet.distanceFromCurrentPosition}km, Rating: Not rated yet`
            },
            info: toilet
          }
          return markerPosition;
        })

      });
  }

  private calculateDistanceInKm(p1: google.maps.LatLngLiteral, p2: google.maps.LatLngLiteral) {
    return +(google.maps.geometry.spherical.computeDistanceBetween(p1, p2) / 1000).toFixed(2);
  }

  ngAfterViewInit(): void {
    // Binding autocomplete to search input control
    let autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement);

    // Align search box to center
    this.map.controls[google.maps.ControlPosition.TOP_LEFT].push(this.searchElementRef.nativeElement);

    autocomplete.addListener('place_changed', () => {
      this.ngZone.run(() => {
        // get the result
        let place: google.maps.places.PlaceResult = autocomplete.getPlace();

        if (place.geometry === undefined || place.geometry === null) {
          return;
        }

        this.center = {
          lat: place.geometry.location?.lat()!,
          lng: place.geometry.location?.lng()!
        };

        this.zoom = 16;

        this.loadToilets();
      })
    })
  }

  openInfoWindow(mapMarker: MapMarker, info: Toilet) {
    if (this.infoWindow != undefined) {
      this.info = info;
      this.infoWindow.open(mapMarker);
    }
    
  }

  

}
