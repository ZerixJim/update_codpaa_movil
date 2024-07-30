<?
include_once('../../connexion/bdManager.php');
$manager = new bdManager();

	$c_menus = "SELECT DISTINCT categoria as text, id_menu_cat as id FROM (
	SELECT DISTINCT menus_cat.`categoria`, menu, url,menus.id_menu,menus.estatus, menus.`id_menu_cat` 
	FROM menus INNER JOIN permisos_clientes ON (menus.id_menu = permisos_clientes.id_menu)
	INNER JOIN menus_cat ON menus.`id_menu_cat` = menus_cat.`id_menu_cat` 
	WHERE id_usuario = '".$_GET['idUsuario']."' ORDER BY menu
	) AS consulta WHERE estatus = '1' ORDER BY categoria";
	
	$r_menus=$manager->ejecutarConsulta($c_menus);
	$i=0;
	$menus_perfil=array();
	while($a_menus = mysqli_fetch_array($r_menus))//aqui 
	{
		$menus_perfil[$i]['id']="c".$a_menus['id'];
			
		$menus_perfil[$i]['text']=utf8_encode($a_menus['text']);	
		
		$menus_perfil[$i]['state']='closed';
	
	$r_menus2 = $manager->ejecutarConsulta("select distinct menus.id_menu as id, menu as text from menus inner join permisos_clientes 
on menus.id_menu = permisos_clientes.id_menu where id_menu_cat = '".$a_menus['id']."' and menus.estatus = 1 and id_usuario = '".$_GET['idUsuario']."' order by menu");
	$j=0;
	while($a_menus2 = mysqli_fetch_array($r_menus2)) //aqui
	{
		$menus_perfil[$i]['children'][$j]['id']=$a_menus2['id'];
		$menus_perfil[$i]['children'][$j]['text']=utf8_encode($a_menus2['text']);	
		$j++;
		}
		$i++;
	}
	$permisos_lista=json_encode($menus_perfil,JSON_PARTIAL_OUTPUT_ON_ERROR);
	
	echo $permisos_lista;
?>
  