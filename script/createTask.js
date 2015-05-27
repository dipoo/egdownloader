var mark = {
    name : ['<h1 id="gn">', '</h1><h1'],
	subname : ['</h1><h1 id="gj">', '</h1></div>'],
	type : ['png" alt="', '" class="ic'],//
	coverUrl : ['<div id="gd1"><img src="', '" alt="" /></div></div>'],
	total : ['Length:</td><td class="gdt2">', ' pages</td></tr><tr><td class="gdt1'],
	size : ['File Size:</td><td class="gdt2">', 'B</td></tr><tr><td class="gdt1">Length', '&nbsp;<span class="halp"'],
	language : ['Language:</td><td class="gdt2">', ' &nbsp;<span class="halp', ' &nbsp;']
};

function interceptFromSource(source, prefix, suffix){
	var s = source;
	s = s.substr(s.indexOf(prefix) + prefix.length, s.length);
    return s.substring(0, s.indexOf(suffix));
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
	task.name = interceptFromSource(source, mark.name[0], mark.name[1]);
	task.subname = interceptFromSource(source, mark.subname[0], mark.subname[1]);
	task.coverUrl = interceptFromSource(source, mark.coverUrl[0], mark.coverUrl[1]);
	task.type = interceptFromSource(source, mark.type[0], mark.type[1]);
	task.total = parseInt(trim(interceptFromSource(source, mark.total[0], mark.total[1])));
	if(source.indexOf(mark.size[1]) != -1){
		task.size = trim(interceptFromSource(source, mark.size[0], mark.size[1]));
	}else{
		task.size = trim(interceptFromSource(source, mark.size[0], mark.size[2]));
	}
	task.language = interceptFromSource(source, mark.language[0], mark.language[1]);
	if(task.language.indexOf(mark.language[2]) > 0){
		task.language = task.language.substr(0, task.language.indexOf(mark.language[2]));
	}
	return parseJson(task);
}
parse(htmlSource);