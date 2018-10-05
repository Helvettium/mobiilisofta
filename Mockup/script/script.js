var currentSite = 'favs';

function changeSite(newSite) {
    switch (newSite) {
        case 'map':
            document.getElementById('mapIcon').style.display = 'none';
            break;
        default:
            document.getElementById('mapIcon').style.display = 'block';
            break;
    }

	document.getElementById('content-view').src='./src/' + newSite + '.html';
	document.getElementsByClassName('button-' + currentSite)[0].src='./img/test_button_' + currentSite + '_n.png';
	document.getElementsByClassName('button-' + newSite)[0].src='./img/test_button_' + newSite + '.png';
	currentSite = newSite;
}
