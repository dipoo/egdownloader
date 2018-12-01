var mark = {
	original : ['https://exhentai.org/fullimg.php', '">Download original'],	
    realUrl : ['<img id="img" src="', '" style=']
};

function interceptFromSource(source, prefix, suffix){
	var s = source;
	s = s.substr(s.indexOf(prefix) + prefix.length, s.length);
    return s.substring(0, s.indexOf(suffix));
}

function parse(source, openhttps){
	if("undefined" != typeof version && "undefined" != typeof down_original && down_original && source.indexOf(mark.original[0]) != -1){
		return mark.original[0].replace("https", "http") + interceptFromSource(source, mark.original[0], mark.original[1]).replace(/&amp;/g, '&');
	}else{
		return interceptFromSource(source, mark.realUrl[0], mark.realUrl[1]).replace("https", openhttps ? "https" : "http");
	}
}
var openhttps = ("undefined" != typeof version && "undefined" != typeof https && https);
parse(htmlSource, openhttps);