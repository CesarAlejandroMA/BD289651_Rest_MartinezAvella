<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Probar API REST</title>
	
	<!-- Se incluye la librería JQuery que facilita interacturar con el DOM de la web -->
	<script type="text/javascript" src="js/jquery-1.12.4.min.js"></script>
	
	<script type="text/javascript">
	 
		function load(x){
			var entry = document.createElement('li');
			
			var a = document.createElement('a');
						
			var linkText = document.createTextNode(" [Borrar]");
						
			a.appendChild(linkText);
						
			a.onclick = function () {
				$.ajax({
				    url: '' + id,
				    type: 'DELETE',
				    dataType: "json",
				    success: function(result) {
				    	document.getElementById(id).remove();
				    },
			    	error: function(jqXhr, textStatus, errorMessage){
				    	alert('error');	
				    }
				});
			};
	
			
			entry.id = id;		
			
			entry.appendChild(document.createTextNode("("+ id + ") " +name + " " + surname));
			 
			entry.appendChild(a);			
			
			$('#users').append(entry);
			
		}
	
		$(document).ready(function(){
			
			$("#sendButton").click(function(){
				
				var sendInfo = {x: $('#x').val()};
				
			    $.ajax({
					    url: '', 
					    headers: { 
				               'Accept': 'application/json',
				               'Content-Type': 'application/json' 
				           },
					    type: 'POST',
					    dataType: "json", 
					    success: function(result) {
					    	load(result.x);
					    },
				    	error: function(jqXhr, textStatus, errorMessage){
					    	alert('Error: ' + jqXhr.responseJSON.resultado);	
					    },
					    data:  JSON.stringify(sendInfo)
					    
					});
			    });
		
			
			$.ajax({
			    url: '',
			    type: 'GET',
			    dataType: "json",
			    success: function(result) {
					jQuery.each(result.users, function(i, val) {
			    		  load(val.x);
			    		});
			    }
			});
		});
</script>
	
	
</head>
<body>

	<center><b>Ejemplo API Rest</b></center><br>
	Formulario para insertar X<br>
	X:<input type=text id="X"><br>	
	<button id="sendButton">Crear</button>
	
	<br>
	<br>
	Listado de X creados
	<br>
	<ul id="X"></ul>
</body>
</html>