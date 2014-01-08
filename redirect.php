<?php
switch($_GET["id"]){
//302临时重定向
case "pxxyps.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload13/%E6%99%AE%E8%B4%A4%E8%8F%A9%E8%90%A8%E8%A1%8C%E6%84%BF%E5%93%81.mp3';
	break;
case "amtf.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload6/wuliang_shoujing_wuxing.mp3';
	break;
case "gsypmp.mp3" :
	$new_url = 'http://file.goodweb.cn/web/d05/PMP04.mp3';
	break;
case "ysf.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload34/%E8%8D%AF%E5%B8%88%E7%BB%8F.mp3';
	break;
case "wssl.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload15/%E6%96%87%E6%AE%8A%E5%B8%88%E5%88%A9%E5%8F%91%E6%84%BF%E7%BB%8F.mp3';
	break;
case "dcw.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload19/%E5%9C%B0%E8%97%8F%E8%8F%A9%E8%90%A8%E6%9C%AC%E6%84%BF%E7%BB%8F.mp3';
	break;
case "xj.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload43/%E5%BF%83%E7%BB%8F_%E5%8A%A0%E9%85%8D%E4%B9%90.mp3';
	break;
case "jgj.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload7/jinggangjing_huiping.mp3';
	break;
case "dbz.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload14/DBZ16.mp3';
	break;
case "dfdsly.mp3" :
	$new_url = 'http://buddha.goodweb.cn/music/musicdownload_all/musicdownload2/LYZ10.mp3';
	break;
case "wmjss.mp3" :
	$new_url = 'http://mp3up.6000y.com/vd.php/17013726/www.6000y.com.mp3';
	break;
}
//header("Location:$new_url");
echo $new_url;
?>