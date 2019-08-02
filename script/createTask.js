var mark = {
	postedTime : ['Posted:</td><td class="gdt2">', '</td></tr><tr><td class="gdt1">Parent'],
    name : ['<h1 id="gn">', '</h1><h1'],
    subname : ['</h1><h1 id="gj">', '</h1></div>'],
    uploader : ['hentai.org/uploader/', '</a></div><div id="gdd">', '">'],
	type : ["document.location='https://e", 'div></div><div id="gdn">', '">', '</'],//
	coverUrl : ['background:transparent url(', ') 0 0 no-repeat"></div>'],
	total : ['Length:</td><td class="gdt2">', ' pages</td></tr><tr><td class="gdt1'],
	size : ['File Size:</td><td class="gdt2">', 'B</td></tr><tr><td class="gdt1">Length', '&nbsp;<span class="halp"'],
	language : ['Language:</td><td class="gdt2">', ' &nbsp;</td></tr><tr><td class="gdt1">File Size:', ' &nbsp;<span class="halp', ' &nbsp;'],
	tagsource:['<div id="taglist">', '</div><div id="tagmenu_act"'],
	tags:['hentai.org/tag/', '" class="', 'toggle_tagmenu']
};

function interceptFromSource(source, prefix, suffix){
	var s = source;
	s = s.substr(s.indexOf(prefix) + prefix.length, s.length);
    return s.substring(0, s.indexOf(suffix));
}
function subFromSource(source, prefix){
	return source.substr(source.indexOf(prefix) + prefix.length, source.length);
}

function trim(s){
	if(s != null && typeof s == 'string'){
		return s.replace(/(^\s*)|(\s*$)/g, "");
	}
	return s;
};

function parseJson(json){
	if(json == null)
		return "";
	var s = "{";
	for(var k in json){
		s += '"' + k + '":';
		if(typeof json[k] == 'number'){
			s += json[k] + ',';
		}else{
			s += '"' + json[k] + '",';
		}
	}
	s = s.substr(0, s.length - 1);
	return s + "}";
}

function parse(source){
	var task = {};
	task.postedTime = interceptFromSource(source, mark.postedTime[0], mark.postedTime[1]);
	task.name = interceptFromSource(source, mark.name[0], mark.name[1]);
	task.subname = interceptFromSource(source, mark.subname[0], mark.subname[1]);
	task.uploader = interceptFromSource(source, mark.uploader[0], mark.uploader[1]).split(mark.uploader[2])[0];
	task.coverUrl = interceptFromSource(source, mark.coverUrl[0], mark.coverUrl[1]).replace("https://ehgt.org", "http://ehgt.org");
	task.type = interceptFromSource(source, mark.type[0], mark.type[1]);
	task.type = interceptFromSource(task.type, mark.type[2], mark.type[3]);
	task.total = parseInt(trim(interceptFromSource(source, mark.total[0], mark.total[1])));
	if(source.indexOf(mark.size[1]) != -1){
		task.size = trim(interceptFromSource(source, mark.size[0], mark.size[1]));
	}else{
		task.size = trim(interceptFromSource(source, mark.size[0], mark.size[2]));
	}
	if(source.indexOf(mark.language[1]) != -1){
		task.language = trim(interceptFromSource(source, mark.language[0], mark.language[1]));
	}else{
		task.language = trim(interceptFromSource(source, mark.language[0], mark.language[2]));
	}
	if(task.language.indexOf(mark.language[3]) > 0){
		task.language = task.language.substr(0, task.language.indexOf(mark.language[3]));
	}
	task.tags = parseTags(source);
	return parseJson(task);
}
function parseTags(source){
	var tagsources = interceptFromSource(source, mark.tagsource[0], mark.tagsource[1]);
	if(tagsources){
		var tags = '';
		while(tagsources.indexOf(mark.tags[0]) != -1){
			var tag = interceptFromSource(tagsources, mark.tags[0], mark.tags[1]);
			if(!tag) break;
			tags += tag + ";";
			tagsources = subFromSource(tagsources, mark.tags[2]);
		}
		return tags;
	}
	return '';
}
parse(htmlSource);