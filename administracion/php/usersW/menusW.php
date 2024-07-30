<? 

include_once('../../connexion/bdManager.php');
$manager = new bdManager();

	$c_menus = "SELECT menus.id_menu_cat as id , menus_cat.categoria as text
	FROM menus 
	INNER JOIN menus_cat ON (menus.id_menu_cat = menus_cat.id_menu_cat) 
	WHERE menus_cat.estatus = '1'  GROUP BY menus_cat.`id_menu_cat` ORDER BY menus_cat.categoria";
	$r_menus =$manager->ejecutarConsulta($c_menus);
	$menus=array();
	$i=0;
	while($a_menus = mysqli_fetch_array($r_menus)) //aqui
		{ 
			$menus[$i]['id']='c'.$a_menus['id'];
			
			$menus[$i]['text']=utf8_encode($a_menus['text']);	
			
			$menus[$i]['state']='closed';
	
		$r_menus2 = $manager->ejecutarConsulta("select distinct id_menu as id,menu as text from menus where id_menu_cat = '".$a_menus['id']."' and estatus = 1 order by menu");
		$j=0;
		while($a_menus2 = mysqli_fetch_array($r_menus2))//aqui 
			{
				
			$menus[$i]['children'][$j]['id']=$a_menus2['id'];
			$menus[$i]['children'][$j]['text']=utf8_encode($a_menus2['text']);
			$j++;
			}
	 $i++;
	}
	$menus_lista=json_encode($menus,JSON_PARTIAL_OUTPUT_ON_ERROR);
	
	echo $menus_lista;
?>
       
     
 
               
        
