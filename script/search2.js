var mark = {
    separator : [',', '###'],
	page : ['Showing ', 'of ', ' results</p><div id="dms">'],
	listSource : ['Thumbnail', '</td></tr></table></div></div>'],
	intercept : ['<tr><td class="gl1m glcat">', '</a></div></td></tr>'],
	name : ['/">', '</a></div><div class="gl3t"'],
	url : ['<a href="', '/">'],
	coverUrl : ['px" src="', '" alt="'],
	filenum : ['></div><div>', ' pages</div>'],
	type : ["document.location='https://exhentai.org/", '/div><div onclick="popUp(', '">', '<'],
	btUrl : ["return popUp('", "', 610, 590)"],
	rate_position: ['<div class="ir" style="background-position:', '; opacity:'],
	rate_mapping: {
		"0px -1px": "5.0",
		"0px -21px": "4.5",
		"-16px -1px": "4.0",
		"-16px -21px": "3.5",
		"-32px -1px": "3.0",
		"-32px -21px": "2.5",
		"-48px -1px": "2.0",
		"-48px -21px": "1.5",
		"-64px -1px": "1.0",
		"-64px -21px": "0.5",
		"-80px -1px": "0.0"
	}
};

function parse(source, openhttps){
	if(source.indexOf(mark.listSource[0]) != -1){
		var page = pageInfo(source);
		source = interceptFromSource(source, mark.listSource[0], mark.listSource[1]);
		var tasks = [];
		var prefix = mark.intercept[1];
		var i = 0;
		while(source.indexOf(mark.intercept[0]) != -1){
			var task = {};
			task.name = interceptFromSource(source, mark.name[0], mark.name[1]);
			task.url = interceptFromSource(source, mark.url[0], mark.url[1]) + "/";
			if(! openhttps){
				task.url = task.url.replace("https", "http");
			}
			task.rating = mark.rate_mapping[interceptFromSource(source, mark.rate_position[0], mark.rate_position[1])];
			task.coverUrl = interceptFromSource(source, mark.coverUrl[0], mark.coverUrl[1]);
			if(! openhttps){
				task.coverUrl = task.coverUrl.replace("https", "http");
			}
			task.filenum = interceptFromSource(source, mark.filenum[0], mark.filenum[1]);
			task.type = interceptFromSource(source, mark.type[0], mark.type[1]);
			task.type = interceptFromSource(task.type, mark.type[2], mark.type[3]);
			var btUrlTemp = interceptFromSource(source, mark.btUrl[0], mark.btUrl[1]);
			if(btUrlTemp && btUrlTemp != ''){
				task.btUrl = btUrlTemp.replace("'", '').replace("'", '').replace("&amp;", "&");
			}
			tasks.push(task);
			source = subFromSource(source, prefix);
			i ++;
		}
		var list = tasks.concat(list);
		return  page + mark.separator[1] + parseJsonArray(list);
	}else{
		return null;
	}
}

function parseJsonArray(array){
	if(array == null)
		return "";
	var s = "[";
	for(var i = 0; i < array.length; i ++){
		s += "{";
		for(var k in array[i]){
			s += '"' + k + '":';
			if(typeof array[i][k] == 'number'){
				s += array[i][k] + ',';
			}else if(typeof array[i][k] == 'boolean'){
				s += array[i][k] + ',';
			}else{
				s += '"' + array[i][k] + '",';
			}
		}
		s = s.substr(0, s.length - 1);
		s += "},";
	}
	s = s.substr(0, s.length - 3);
	return s + "]";
}

function interceptFromSource(source, prefix, suffix){
	var s = source;
	s = s.substr(s.indexOf(prefix) + prefix.length, s.length);
    return s.substring(0, s.indexOf(suffix));
}

function subFromSource(source, prefix){
	return source.substr(source.indexOf(prefix) + prefix.length, source.length);
}

function pageInfo(source){
	var s = source;
	var count = null;
	if(s.indexOf(mark.page[0]) != -1){
		s = subFromSource(s, mark.page[0]);
		count = interceptFromSource(s, mark.page[1], mark.page[2]).replace(",", "");
	}
	if(count == null){
		return null;
	}
	return count + mark.separator[0] + (parseInt(count) % 25 == 0 ? Math.round(parseInt(count) / 25) : Math.round(parseInt(count) / 25 + 1));
}
    
       
var openhttps = ("undefined" != typeof version && "undefined" != typeof https && https);
parse(htmlSource, openhttps);