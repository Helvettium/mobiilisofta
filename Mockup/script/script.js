var currentSite = 'favs';

function changeSite(newSite) {
	document.getElementById('content-view').src='./src/' + newSite + '.html';
    
    let newButton = document.getElementsByClassName('button-' + newSite);
    let oldButton = document.getElementsByClassName('button-' + currentSite);

    if (typeof newButton[0] !== 'undefined') {
        document.getElementsByClassName('button-' + newSite)[0].src='./img/test_button_' + newSite + '.png';
    }
    
    if (typeof oldButton[0] !== 'undefined') {
        document.getElementsByClassName('button-' + currentSite)[0].src='./img/test_button_' + currentSite + '_n.png';
    }
    
    if (newSite == 'map') {
        document.getElementById('mapIcon').style.display = 'none';
    }
    else {
        document.getElementById('mapIcon').style.display = 'block';
    }

	currentSite = newSite;
}
