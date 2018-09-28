var changeFrom = 'favs';

function changeSite(var changeTo) {
	document.getElementById('content-view').src='./src/' + changeTo + '.html';
	document.getElementsByClassName('button-' + changeFrom)[0].src='./img/test_button_n_pressed.png';
	document.getElementsByClassName('button-' + changeTo)[0].src='./img/test_button_pressed.png';
	changeFrom = changeTo;
}