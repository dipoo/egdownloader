var mark = {
	realUrl : ['<img id="img" src="', '" style=']
};

function interceptFromSource(source, prefix, suffix){
	var s = source;
	s = s.substr(s.indexOf(prefix) + prefix.length, s.length);
    return s.substring(0, s.indexOf(suffix));
}

function parse(source){
	var realUrl = interceptFromSource(source, mark.realUrl[0], mark.realUrl[1]);
	return realUrl;
}
parse(htmlSource);