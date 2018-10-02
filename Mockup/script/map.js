'use strict';

class Map
{
    constructor()
    {
        $('body').empty();

        this.$map = $('<div>', {'id': 'map'}).appendTo($('body'));
        this.map = new google.maps.Map(this.$map.get(0), {'center': new google.maps.LatLng(61.502545, 23.807463), zoom: 16});

        this.$map.height($(document).height() + 'px');
    }


}



/*
RotationMap.prototype.initMap = function(aElement)
{
    this.map = new google.maps.Map(document.getElementById(aElement), {center: new google.maps.LatLng(60, 20), zoom: 5, minZoom: 3, maxZoom: 8});
    this.scale = 1 / (Math.pow(2, -(this.map.getZoom())));

    this.createPortMarkers();
    this.createLineMarkers();

    google.maps.event.addListener(this.map, 'projection_changed', function() {this.updateLines()}.bind(this));
    google.maps.event.addListener(this.map, 'zoom_changed', function() {this.updateScales()}.bind(this));
};


let marker = {id: id, marker: new google.maps.Marker({position: new google.maps.LatLng(aLat, aLng), label: aLabel, zIndex: this.idCount, draggable: true, map: this.map})};
    marker.marker.addListener('dragend', (aEvent) => {this.handleDrag(aEvent, marker.id)}, false);
*/
