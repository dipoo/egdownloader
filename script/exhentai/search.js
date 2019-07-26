var mark = {
    separator : [',', '###'],
	page : ['Showing', ' ', ' results</p><div id="dms">'],
	listSource : ['Uploader</th></tr>', '</td></tr></table></div></div>'],
	intercept : ['<td class="gl1c glcat">', "pages</div></td></tr>"],
	name : ['<div class="glink">', '</div><div><div class="gt"', '</div><div>&nbsp;</div></a>'],
	url : [')"><a href="', '"><div class="glink">', ",'", "')"],
	coverUrl : ['" src="', '" /></div>', 'data-src="', '.jpg" />'],
	date : ['" id="posted_', '/div><div class="ir" style="background-position', '">', '<'],
	type : ["document.location='https://exhentai.org/", '/div></td><td class="gl2c">', '">', '<'],
	filenum : ['https://exhentai.org/uploader/', 'pages</div>', '</a></div><div>', ' '],
	btUrl : ["return popUp('", "', 610, 590)"],
	uploader : ['exhentai.org/uploader', 'pages</div></td></tr>', '>', '<'],
	tags : ['<div><div class="gt" ', '</div></div></a></td>', 'title="', '">'],
	rate_position: ['<div class="ir" style="background-position:', ';opacity:'],
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

function parseJsonArray(array){
	if(array == null)
		return "";
	var s = "[";
	for(var i = 0; i < array.length; i ++){
		s += "{";
		for(var k in array[i]){
			if(array[i][k]){
				s += '"' + k + '":';
				if(typeof array[i][k] == 'number'){
					s += array[i][k] + ',';
				}else if(typeof array[i][k] == 'boolean'){
					s += array[i][k] + ',';
				}else{
					s += '"' + array[i][k] + '",';
				}
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
	return count + mark.separator[0] + (parseInt(count) % 25 == 0 ? parseInt(count / 25) : parseInt(count / 25) + 1);
}
    
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
			if(task.name.indexOf(mark.name[2]) != -1){
				task.name = interceptFromSource(source, mark.name[0], mark.name[2]);
			}
			
			task.url = interceptFromSource(source, mark.url[0], mark.url[1]);
			if(! openhttps){
				task.url = task.url.replace("https", "http");
			}
			task.rating = mark.rate_mapping[interceptFromSource(source, mark.rate_position[0], mark.rate_position[1])];
			if(i == 0){
				task.coverUrl = interceptFromSource(source, mark.coverUrl[0], mark.coverUrl[1]);
				if(! openhttps){
					task.coverUrl = task.coverUrl.replace("https", "http");
				}
			}else{
				task.coverUrl = interceptFromSource(source, mark.coverUrl[2], mark.coverUrl[3]) + ".jpg";
				if(! openhttps){
					task.coverUrl = task.coverUrl.replace("https", "http");
				}
			}
			if(task.coverUrl){
				task.coverUrl = task.coverUrl.replace("250.jpg", "l.jpg");
			}
			task.date = interceptFromSource(source, mark.date[0], mark.date[1]).replace("<s>", "").replace("</s>", "");
			task.date = interceptFromSource(task.date, mark.date[2], mark.date[3]);
			task.type = interceptFromSource(source, mark.type[0], mark.type[1]);
			task.type = interceptFromSource(task.type, mark.type[2], mark.type[3]);
			task.filenum = interceptFromSource(source, mark.filenum[0], mark.filenum[1]);
			task.filenum = interceptFromSource(task.filenum, mark.filenum[2], mark.filenum[3]);
			var btUrlTemp = interceptFromSource(source, mark.btUrl[0], mark.btUrl[1]);
			if(btUrlTemp && btUrlTemp != ''){
				task.btUrl = btUrlTemp.replace("&amp;", "&");
				if(task.url.indexOf(subFromSource(task.btUrl, "gid=").replace("&t=", "/")) == -1){
					task.btUrl = null;
				}
			}
			task.uploader = interceptFromSource(source, mark.uploader[0], mark.uploader[1]);
			task.uploader = interceptFromSource(task.uploader, mark.uploader[2], mark.uploader[3]);
			
			task.tags = '';
			var tagstemp = interceptFromSource(source, mark.tags[0], mark.tags[1]);
			while(tagstemp.indexOf(mark.tags[2]) != -1){
				task.tags += interceptFromSource(tagstemp, mark.tags[2], mark.tags[3]) + ";";
				tagstemp = subFromSource(tagstemp, mark.tags[2]);
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
var openhttps = ("undefined" != typeof version && "undefined" != typeof https && https);
parse(htmlSource, openhttps);