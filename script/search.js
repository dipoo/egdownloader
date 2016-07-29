var mark = {
    separator : [',', '###'],
	page : ['Showing ', 'of ', '</p><table class="ptt'],
	listSource : ['Uploader</th></tr>', '<table class="ptb"'],
	intercept : ['<tr class="gtr', "</td></tr>"],
	name : ['hide_image_pane', '/a></div><div class=', ')">', '<'],
	url : ['class="it5"><a href="', '" onmouseover'],
	coverUrl : ['px"><img src="', '" alt="', '~exhentai.org~', '~'],
	date : ['white-space:nowrap">', '</td><td class="itd" onmouseover'],
	type : ['.png" alt="', '" class="ic" />'],
	btUrl : ['</tr>', "return popUp('", "', 610, 590)"],
	uploader : ['exhentai.org/uploader', '>', '</a>']
};

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
    
function parse(source){
	if(source.indexOf(mark.listSource[0]) != -1){
		var page = pageInfo(source);
		source = interceptFromSource(source, mark.listSource[0], mark.listSource[1]);
		var tasks = [];
		var prefix = mark.intercept[1];
		var i = 0;
		while(source.indexOf(mark.intercept[0]) != -1){
			var task = {};
			var nameTemp = interceptFromSource(source, mark.name[0], mark.name[1]);
			task.name = interceptFromSource(nameTemp, mark.name[2], mark.name[3]);
			task.url = interceptFromSource(source, mark.url[0], mark.url[1]);
			task.url = task.url.replace("https", "http");
			if(i == 0){
				task.coverUrl = interceptFromSource(source, mark.coverUrl[0], mark.coverUrl[1]);
				task.coverUrl = task.coverUrl.replace("https", "http");
			}else{
				task.coverUrl = "http://exhentai.org/" + interceptFromSource(source, mark.coverUrl[2], mark.coverUrl[3] + task.name);
			}
			task.date = interceptFromSource(source, mark.date[0], mark.date[1]);
			task.type = interceptFromSource(source, mark.type[0], mark.type[1]);
			var btUrlTemp = source.substring(0, source.indexOf(mark.btUrl[0]));
			if(btUrlTemp.indexOf(mark.btUrl[1]) != -1){
				task.btUrl = interceptFromSource(btUrlTemp, mark.btUrl[1], mark.btUrl[2]).replace("&amp;", "&");
			}
			var uploaderTemp = subFromSource(source, mark.uploader[0]);
			task.uploader = interceptFromSource(uploaderTemp, mark.uploader[1], mark.uploader[2]);
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
parse(htmlSource);