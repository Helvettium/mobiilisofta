'use strict';

function mapCallback()
{
    window.map = new Map(4);
}

class Map
{
    constructor(divider)
    {
        this.$map = $('<div>', {'id': 'map'}).appendTo($('#mapGuide'));
        this.map = new google.maps.Map(this.$map.get(0), {'center': new google.maps.LatLng(61.502545, 23.807463), zoom: 16});

        this.$map.height($(document).height()/divider + 'px');

        let marker1 = new google.maps.Marker({'position': new google.maps.LatLng(61.502451, 23.806198), map: this.map});
            marker1.addListener('click', (aEvent) => {this.handleClick(aEvent, 0)}, false);

        let marker2 = new google.maps.Marker({'position': new google.maps.LatLng(61.502249, 23.807944), map: this.map});
            marker2.addListener('click', (aEvent) => {this.handleClick(aEvent, 1)}, false);

        let marker3 = new google.maps.Marker({'position': new google.maps.LatLng(61.502742, 23.805301), map: this.map});
            marker3.addListener('click', (aEvent) => {this.handleClick(aEvent, 2)}, false);

        let marker4 = new google.maps.Marker({'position': new google.maps.LatLng(61.503110, 23.812554), map: this.map});
            marker4.addListener('click', (aEvent) => {this.handleClick(aEvent, 3)}, false);
    }

    handleClick(aEvent, aID)
    {
        console.log(aID);
        this.map.panTo(aEvent.latLng);
    }
}
