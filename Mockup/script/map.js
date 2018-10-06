'use strict';

function mapCallback()
{
    window.map = new Map();
}

class Map
{
    constructor()
    {
        $('body').empty();

        this.$map = $('<div>', {'id': 'map'}).appendTo($('body'));
        this.$stop = $('<div>', {'id': 'stop'}).appendTo($('body'));

        this.map = new google.maps.Map(this.$map.get(0), {'center': new google.maps.LatLng(61.502545, 23.807463), zoom: 16});

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
        this.$stop.empty().append(this.appendStop(aID));

        this.map.panTo(aEvent.latLng);

        if (this.$stop.height() < 10)
        {
            this.$map.animate({'height': ($(document).height() - 200) + 'px'}, 500);
            this.$stop.animate({'height': '200px'}, 500);
        }
    }

    appendStop(aID)
    {
        let $table = $('<table>');

        switch (aID)
        {
            case 0:
                 $('<tr>').append($('<th>', {'text': 'Pysäkki 1', 'colspan': 2})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 1'})).append($('<td>', {'text': '13:20'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 1a'})).append($('<td>', {'text': '13:25'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 3'})).append($('<td>', {'text': '13:42'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 4'})).append($('<td>', {'text': '14:00'})).appendTo($table);
                break;

            case 1:
                 $('<tr>').append($('<th>', {'text': 'Pysäkki 2', 'colspan': 2})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 5'})).append($('<td>', {'text': '12:50'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 7'})).append($('<td>', {'text': '14:14'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 12a'})).append($('<td>', {'text': '14:36'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 12b'})).append($('<td>', {'text': '15:00'})).appendTo($table);
                break;

            case 2:
                 $('<tr>').append($('<th>', {'text': 'Pysäkki 3', 'colspan': 2})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 8'})).append($('<td>', {'text': '13:32'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 6'})).append($('<td>', {'text': '13:44'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 3'})).append($('<td>', {'text': '14:15'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 9'})).append($('<td>', {'text': '14:21'})).appendTo($table);
                break;

            case 3:
                 $('<tr>').append($('<th>', {'text': 'Pysäkki 4', 'colspan': 2})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 19'})).append($('<td>', {'text': '13:16'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 6'})).append($('<td>', {'text': '13:22'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 10'})).append($('<td>', {'text': '13:26'})).appendTo($table);
                 $('<tr>').append($('<td>', {'text': 'Linja 15'})).append($('<td>', {'text': '13:38'})).appendTo($table);
                break;
        }

        return $table;
    }
}
