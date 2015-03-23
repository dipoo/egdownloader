var mark = {//标志符
	listSource : ['</table><div id="gdt">', '<div class="c"></div></div><table'],//每页所有图片源码
	intercept : ['style="height', "gdtm"],//判断是否还有及截取剩余字符串
	showUrl : ['no-repeat"><a href="', '"><img alt='],//显示url
	name : ['title="', '" src=']//名称
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
	var prefix = mark.intercept[1];//截取的标志
	temp = subFromSource(temp, prefix);
	while(temp.indexOf(mark.intercept[0]) != -1){
		var picture = {};
		//获取图片浏览地址
		picture.url = interceptFromSource(temp, mark.showUrl[0], mark.showUrl[1]);
		//获取图片名称
		var s = interceptFromSource(temp, mark.name[0], mark.name[1]);//Page 1: img00001.jpg
		picture.name = trim(s.split(':')[1]);
		newpics.push(picture);
		temp = subFromSource(temp, prefix);
	}
	var pictures = newpics.concat(pictures);
	return parseJsonArray(pictures);
}       
parse(htmlSource);