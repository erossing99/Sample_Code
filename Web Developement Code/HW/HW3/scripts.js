// function chgimg(name, txt, smallimg) {
//   var theimage = document.getElementById("goldy");
//   var small = document.getElementById(smallimg);
//   small.style.visibility = "visible";
//   theimage.src = name;
//   theimage.alt = txt;
// }

function randomImage(){
  var pictures = ["08ShepherdLabs960.jpg", "kellerhall.jpg", "northrop.jpg", "reccenter.jpg", "themarshall.jpg"];
  var descriptions = ["Shepherd Labs", "Keller Hall", "Northrop Auditorium", "Recwell", "The Marshall"];
  var index = Math.floor(Math.random() * 5);
  var img = document.getElementById("goldy");
  img.setAttribute("src", pictures[index]);
  img.setAttribute("alt", descriptions[index]);
}

var initialDegreeRotate = 0;
function rotateImage(){
  initialDegreeRotate = initialDegreeRotate + 1;
  var img = document.getElementById("goldy");
  img.style.transform = ('rotate(' + initialDegreeRotate + 'deg)');
}

var rotating = 0; //0 means false, 1 means true
var imgRotate = 0; //initial null value
function startRotate(){
  if (rotating == 0){ //not rotating
    imgRotate = setInterval(rotateImage, 50);
    rotating = 1;
  }
  else if (rotating == 1){ //is rotating
    stopRotate(imgRotate);
    rotating = 0;
  }
}

function stopRotate(imgRotate){
  imgRotate = clearInterval(imgRotate);
}

var geocoder;
var map;
var directionsService;
var directionsRenderer;
function initMap(){ //map initializer for contacts page
  directionsService = new google.maps.DirectionsService();
  directionsRenderer = new google.maps.DirectionsRenderer();
  geocoder = new google.maps.Geocoder();
  var myLatLng = {lat:44.9727, lng:-93.23540000000003};
  map = new google.maps.Map(document.getElementById("map"),{
    center: myLatLng,
    zoom: 14
  });
  directionsRenderer.setMap(map);
  directionsRenderer.setPanel(document.getElementById("left-panel"));
  populateMap();
}

function initMapForm(){ //map initializer for forms page
  directionsService = new google.maps.DirectionsService();
  directionsRenderer = new google.maps.DirectionsRenderer();
  geocoder = new google.maps.Geocoder();
  var myLatLng = {lat:44.9727, lng:-93.23540000000003};
  map = new google.maps.Map(document.getElementById("map"),{
    center: myLatLng,
    zoom: 14
  });
}

function populateMap(){
  var names = ["row1_Name", "row2_Name", "row3_Name", "row4_Name", "row5_Name"];
  var addresses = ["row1_Address", "row2_Address", "row3_Address", "row4_Address", "row5_Address"];
  var infos = ["row1_ContactInfo", "row2_ContactInfo", "row3_ContactInfo", "row4_ContactInfo", "row5_ContactInfo"];

  var currentName;
  var currentAddress;
  var currentInfo;
  var i;
  for(i = 0; i < 5; i++){
     currentName = document.getElementById(names[i]).innerText;
     currentAddress = document.getElementById(addresses[i]).innerText;
     currentInfo = document.getElementById(infos[i]).innerText;
     addMarker(currentName, currentAddress, currentInfo);
  }
}

var markers = [];
//utilizing code from Google Maps Platform Geocoding Api tutorials
function addMarker(currentName, currentAddress, currentInfo){
  geocoder.geocode({'address': currentAddress}, (results, status)=>{
    if (status == 'OK'){
      //map.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
        map: map,
        position: results[0].geometry.location});
      markers.push(marker);
      var infoWindow = new google.maps.InfoWindow({
        content: '<h3>' + currentName + '</h3>' + '<p>' + currentInfo + '</p>' + '<p>' + currentAddress + '</p>'
      });
      google.maps.event.addListener(marker, 'click', function(){
        infoWindow.open(map, marker);
      });
    }
    else{
      alert('Geocode was not successful');
    }
  });
}

function enableOtherPlace(){
  var dropDownSelection = document.getElementById("placeSelection").value;
  if (dropDownSelection == 'other'){
    document.getElementById('otherPlace').disabled = false;
  }
  else{
    document.getElementById('otherPlace').value = '';
    document.getElementById('otherPlace').disabled = true;
  }
}

//utilizing code from Google Places Libarary Documentation
function placeSearch(){
  var dropDownSelection = document.getElementById("placeSelection").value;
  if (dropDownSelection == 'other'){
    var dropDownSelection = document.getElementById("otherPlace").value;
  }
  var searchRadius = document.getElementById("searchRadius").value;
  var mapCenter = new google.maps.LatLng(44.9727,-93.23540000000003);

  var request = {
    location : mapCenter,
    radius : searchRadius,
    query : dropDownSelection
  };
  var service = new google.maps.places.PlacesService(map);
  service.textSearch(request, callback);
}

//utilizing code from Google Places Library Documentation
function callback(results, status){
  if (status == google.maps.places.PlacesServiceStatus.OK){
    clearMarkers();
    for (var i = 0; i < results.length; i++) {
      var place = results[i];
      var marker = new google.maps.Marker({
        map: map,
        position: place.geometry.location});
      markers.push(marker);
      addInfoWindow(marker, place);
    }
  }
}

function addInfoWindow(marker, place){
  var infoWindow = new google.maps.InfoWindow({
    content: '<h3>' + place.name + '</h3>' + '<p>' + 'Address: ' + place.formatted_address + '</p>'
  });
  google.maps.event.addListener(marker, 'click', function(){
    infoWindow.open(map, marker);
  });
}

function clearMarkers(){
   for (var i = 0; i < markers.length; i++){
     markers[i].setMap(null);
   }
   markers = [];
}

function getPosition(){
  if (navigator.geolocation){
    navigator.geolocation.getCurrentPosition(getDirections);
  }
  else{
    alert("Geolocator not supported by this browser.");
  }
  //call getCurrentPosition in here and pass getDirections as callback function
}

function getDirections(position){
  clearMarkers();
  var lat = position.coords.latitude;
  var lng = position.coords.longitude;
  var start = new google.maps.LatLng(lat,lng);
  var destination = document.getElementById("destination").value;
  var travelMode;
  if (document.getElementById("walk").checked == true){
    travelMode = 'WALKING'
  }
  else if (document.getElementById("drive").checked == true){
    travelMode = 'DRIVING'
  }
  else if (document.getElementById("transit").checked == true){
    travelMode = 'TRANSIT'
  };
  var request = {
    origin: start,
    destination: destination,
    travelMode: travelMode
  }
  directionsService.route(request, function(result, status) {
    if (status == 'OK') {
      directionsRenderer.setDirections(result);
    }
  });
  document.getElementById("left-panel").style.display = "inline-block";
}
