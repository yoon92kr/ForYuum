function isNull(value){
	return value == null || value == 'null' || value == 'undefined' || value == '';
}

function isNotNull(value){
	return !isNull(value);
}