package org.arong.egdownloader.ui;

import javax.swing.ImageIcon;
/**
 * 图标管理器
 * @author dipoo
 * @since 2015-05-16
 *
 */
public class IconManager {
	
	private static String skinPath = ComponentConst.ICON_PATH + ComponentConst.SKIN_NUM;
	
	private static ImageIcon addImage;
	private static ImageIcon changeImage;
	private static ImageIcon loadingImage;
	private static ImageIcon folderImage;
	private static ImageIcon openpicImage;
	private static ImageIcon operaImage;
	
	private static ImageIcon deleteImage;
	private static ImageIcon settingImage;
	private static ImageIcon downloadImage;
	private static ImageIcon selectImage;
	private static ImageIcon sizeImage;
	private static ImageIcon toolImage;
	private static ImageIcon pictureImage;
	private static ImageIcon userImage;
	private static ImageIcon saveImage;
	private static ImageIcon detailImage;
	private static ImageIcon copyImage;
	private static ImageIcon cutImage;
	private static ImageIcon pasteImage;
	private static ImageIcon browseImage;
	private static ImageIcon checkImage;
	private static ImageIcon resetImage;
	private static ImageIcon okImage;
	private static ImageIcon startImage;
	private static ImageIcon stopImage;
	private static ImageIcon countImage;
	private static ImageIcon groupImage;
	private static ImageIcon clearImage;
	private static ImageIcon taskImage;
	private static ImageIcon initImage;
	private static ImageIcon leftImage;
	private static ImageIcon rightImage;
	private static ImageIcon zipImage;
	private static ImageIcon previewImage;
	private static ImageIcon failImage;
	
	private static ImageIcon ehImage;
	private static ImageIcon tImage;
	private static ImageIcon artistcgImage;
	private static ImageIcon asianpornImage;
	private static ImageIcon cosplayImage;
	private static ImageIcon doujinshiImage;
	private static ImageIcon gamecgImage;
	private static ImageIcon imagesetImage;
	private static ImageIcon mangaImage;
	private static ImageIcon miscImage;
	private static ImageIcon non_hImage;
	private static ImageIcon westernImage;
	
	public static ImageIcon getIcon(String name){
		if("add".equals(name)){
			if(addImage == null){
				addImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("add")));
			}
			return addImage;
		}else if("change".equals(name)){
			if(changeImage == null){
				changeImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("change")));
			}
			return changeImage;
		}else if("folder".equals(name)){
			if(folderImage == null){
				folderImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("folder")));
			}
			return folderImage;
		}else if("openpic".equals(name)){
			if(openpicImage == null){
				openpicImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("openpic")));
			}
			return openpicImage;
		}else if("opera".equals(name)){
			if(operaImage == null){
				operaImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("opera")));
			}
			return operaImage;
		}
		
		else if("delete".equals(name)){
			if(deleteImage == null){
				deleteImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("delete")));
			}
			return deleteImage;
		}
		else if("download".equals(name)){
			if(downloadImage == null){
				downloadImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("download")));
			}
			return downloadImage;
		}
		else if("select".equals(name)){
			if(selectImage == null){
				selectImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("select")));
			}
			return selectImage;
		}
		else if("setting".equals(name)){
			if(settingImage == null){
				settingImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("setting")));
			}
			return settingImage;
		}
		else if("size".equals(name)){
			if(sizeImage == null){
				sizeImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("size")));
			}
			return sizeImage;
		}
		else if("tool".equals(name)){
			if(toolImage == null){
				toolImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("tool")));
			}
			return toolImage;
		}
		
		else if("picture".equals(name)){
			if(pictureImage == null){
				pictureImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("picture")));
			}
			return pictureImage;
		}
		else if("user".equals(name)){
			if(userImage == null){
				userImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("user")));
			}
			return userImage;
		}
		else if("save".equals(name)){
			if(saveImage == null){
				saveImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("save")));
			}
			return saveImage;
		}
		else if("detail".equals(name)){
			if(detailImage == null){
				detailImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("detail")));
			}
			return detailImage;
		}
		else if("copy".equals(name)){
			if(copyImage == null){
				copyImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("copy")));
			}
			return copyImage;
		}
		else if("cut".equals(name)){
			if(cutImage == null){
				cutImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("cut")));
			}
			return cutImage;
		}
		else if("paste".equals(name)){
			if(pasteImage == null){
				pasteImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("paste")));
			}
			return pasteImage;
		}
		else if("browse".equals(name)){
			if(browseImage == null){
				browseImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("browse")));
			}
			return browseImage;
		}
		else if("check".equals(name)){
			if(checkImage == null){
				checkImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("check")));
			}
			return checkImage;
		}
		else if("reset".equals(name)){
			if(resetImage == null){
				resetImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("reset")));
			}
			return resetImage;
		}
		else if("ok".equals(name)){
			if(okImage == null){
				okImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("ok")));
			}
			return okImage;
		}
		else if("start".equals(name)){
			if(startImage == null){
				startImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("start")));
			}
			return startImage;
		}
		else if("stop".equals(name)){
			if(stopImage == null){
				stopImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("stop")));
			}
			return stopImage;
		}
		else if("count".equals(name)){
			if(countImage == null){
				countImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("count")));
			}
			return countImage;
		}
		else if("group".equals(name)){
			if(groupImage == null){
				groupImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("group")));
			}
			return groupImage;
		}
		else if("clear".equals(name)){
			if(clearImage == null){
				clearImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("clear")));
			}
			return clearImage;
		}
		else if("task".equals(name)){
			if(taskImage == null){
				taskImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("task")));
			}
			return taskImage;
		}else if("zip".equals(name)){
			if(zipImage == null){
				zipImage = new ImageIcon(IconManager.class.getResource(skinPath + ComponentConst.SKIN_ICON.get("zip")));
			}
			return zipImage;
		}else if("init".equals(name)){
			if(initImage == null){
				initImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "init.jpg"));
			}
			return initImage;
		}else if("fail".equals(name)){
			if(failImage == null){
				failImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "fail.png"));
			}
			return failImage;
		}else if("preview".equals(name)){
			if(previewImage == null){
				previewImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "preview.png"));
			}
			return previewImage;
		}else if("left".equals(name)){
			if(leftImage == null){
				leftImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "left.png"));
			}
			return leftImage;
		}else if("right".equals(name)){
			if(rightImage == null){
				rightImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "right.png"));
			}
			return rightImage;
		}
		
		else if("loading".equals(name)){
			if(loadingImage == null){
				loadingImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "loading.gif"));
			}
			return loadingImage;
		}else if("eh".equals(name)){
			if(ehImage == null){
				ehImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "eh.png"));
			}
			return ehImage;
		}else if("t".equals(name)){
			if(tImage == null){
				tImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "t.png"));
			}
			return tImage;
		}else if("artistcg".equals(name) || "artist cg sets".equals(name) || "artist cg".equals(name)){
			if(artistcgImage == null){
				artistcgImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "ARTISTCG.png"));
			}
			return artistcgImage;
		}else if("asianporn".equals(name) || "asian porn".equals(name)){
			if(asianpornImage == null){
				asianpornImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "ASIANPORN.png"));
			}
			return asianpornImage;
		}else if("cosplay".equals(name)){
			if(cosplayImage == null){
				cosplayImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "COSPLAY.png"));
			}
			return cosplayImage;
		}else if("doujinshi".equals(name)){
			if(doujinshiImage == null){
				doujinshiImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "DOUJINSHI.png"));
			}
			return doujinshiImage;
		}else if("gamecg".equals(name) || "game cg".equals(name)){
			if(gamecgImage == null){
				gamecgImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "GAMECG.png"));
			}
			return gamecgImage;
		}else if("imageset".equals(name) || "image sets".equals(name) || "image set".equals(name)){
			if(imagesetImage == null){
				imagesetImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "IMAGESET.png"));
			}
			return imagesetImage;
		}else if("manga".equals(name)){
			if(mangaImage == null){
				mangaImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "MANGA.png"));
			}
			return mangaImage;
		}else if("misc".equals(name)){
			if(miscImage == null){
				miscImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "MISC.png"));
			}
			return miscImage;
		}else if("non-h".equals(name)){
			if(non_hImage == null){
				non_hImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "NON-H.png"));
			}
			return non_hImage;
		}else if("western".equals(name)){
			if(westernImage == null){
				westernImage = new ImageIcon(IconManager.class.getResource(ComponentConst.ICON_PATH + "WESTERN.png"));
			}
			return westernImage;
		}
		return null;
	}
}
