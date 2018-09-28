var currentSite = 'favs';

function changeSite(newSite) {
	document.getElementById('content-view').src='./src/' + newSite + '.html';
	document.getElementsByClassName('button-' + currentSite)[0].src='./img/test_button_n_pressed.png';
	document.getElementsByClassName('button-' + newSite)[0].src='./img/test_button_pressed.png';
	currentSite = newSite;
}
