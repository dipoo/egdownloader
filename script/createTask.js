var mark = {//标志符
	name : ['<h1 id="gn">', '</h1><h1'],//标题
	subname : ['</h1><h1 id="gj">', '</h1></div>'],//小标题
	type : ['png" alt="', '" class="ic'],//类别
	coverUrl : ['<div id="gd1"><img src="', '" alt="" /></div></div>'],//封面url
	total_size : ['Images:</td><td class="gdt2">', '</td></tr><tr><td class="gdt1">Resized:'],//数目@大小
	language : ['Language:</td><td class="gdt2">', '</td></tr></table></div><div id="gdr"']//漫画语言
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
	//获取标题
	task.name = interceptFromSource(source, mark.name[0], mark.name[1]);
	//获取小标题
	task.subname = interceptFromSource(source, mark.subname[0], mark.subname[1]);
	//获取封面路径
	task.coverUrl = interceptFromSource(source, mark.coverUrl[0], mark.coverUrl[1]);
	//获取类别
	task.type = interceptFromSource(source, mark.type[0], mark.type[1]);
	//获取数目及大小
	var temp = interceptFromSource(source, mark.total_size[0], mark.total_size[1]);
	task.total = parseInt(trim(temp.split("@")[0]));
	task.size = trim(temp.split("@")[1]);
	//获取漫画语言
	task.language = interceptFromSource(source, mark.language[0], mark.language[1]);
	return parseJson(task);
}
parse(htmlSource);