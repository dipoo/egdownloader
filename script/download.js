var mark = {//标志符
	realUrl : ['<img id="img" src="', '" style=']//真实下载地址
};

function interceptFromSource(source, prefix, suffix){
	var s = source;
	s = s.substr(s.indexOf(prefix) + prefix.length, s.length);
    return s.substring(0, s.indexOf(suffix));
}

function download(source){
	var realUrl = interceptFromSource(source, mark.realUrl[0], mark.realUrl[1]);
	return realUrl;
}
download(htmlSource);