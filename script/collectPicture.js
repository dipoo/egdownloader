var mark = {//
    listSource : ['<div id="gdt">', '<div id="cdiv" class="gm">'],//
	intercept : ['style="height', "gdtl"],//
	interceptReg : /<div class="gdt\w" style="height:\d+px">((?!<\/div>).)*<\/div>/g,
	showUrl : ['"><a href="', '"><img alt='],//
	name : ['title="', '" src=']//
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

function trim(s){
	if(s != null && typeof s == 'string'){
		return s.replace(/(^\s*)|(\s*$)/g, "");
	}
	return s;
};
    
function parse(temp){
	var newpics = [];
	var count=0;
	var tmp;
	print("start");
	while(tmp = mark.interceptReg.exec(temp)){
		print(tmp[0] + "test " + count+ "\n");
		var targetStr = tmp[0];
		count ++;
		var picture = {};
		//
		picture.url = interceptFromSource(temp, mark.showUrl[0], mark.showUrl[1]);
		picture.url = picture.url.replace("https", "http");
		//
		var s = interceptFromSource(temp, mark.name[0], mark.name[1]);//Page 1: img00001.jpg
		picture.name = trim(s.split(':')[1]);
		newpics.push(picture);
	}
	var pictures = newpics.concat(pictures);
	return parseJsonArray(pictures);
}       
parse(htmlSource);
