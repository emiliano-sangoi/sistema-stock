/**
 * 
 */

$(document).ready(function(){
	
	var formPedido = $('#idFormPedido');
	
	//div contenedor de los detalles de pedido:
	var container = $('#items-container');
	
	$('#btn-add-item').click(function(e){
		e.preventDefault();
		
		//obtener pos ultimo elemento insertado		
		var proximo = formPedido.data("proximo");
		
		var item_tpl = $("#item-template").clone();
		item_tpl.removeClass("hidden");				
				
		// Agregar items al listado			
		/*var codProdInput = $('<input/>');
		var cantInput = $('<input/>');*/
				
		//inputs
		var id = 'products' + last;
		var name = 'products[' + last + ']';
		
		//copiar template de un item del detalle:
		var item = $('#items-container > div:first-child').clone();
		
		
		//id de la fila:
		console.log(item)
		item.addClass("item" + last);
		item.find("button").data("idx", 88);
		
		//actualizar codigo del producto con el indice correspondiente:
		item.find(".item-cod-prod").each(function(index){
			$(this)
				.attr("path", name + '.id' )
				.attr("id", id + '.id')
				.attr("name", name + '.id' )
				.attr("value", "");
		});
		
		//actualizar cantidad del producto con el indice correspondiente
		item.find(".item-cant-prod").each(function(index){
			$(this)
				.attr("path", name + '.count' )
				.attr("id", id + '.count')
				.attr("name", name + '.count' )
				.attr("value", "");
		});
		
		console.log(item);
		
		//agregar item al listado:
		container.append(item).hide().fadeIn();
		
		//actualizar indice al ultimo elemento:
		//console.log(last + 1);
		formPedido.data("idx-ultimo", last + 1);
		
	});
	
	$('.agregar-item').click(function(e){
		e.preventDefault();
		
		$("#input-op").attr("value", ACTION_NUEVO_ITEM);
		
		$("form").submit();
		
		
	});
	
	$('.guardar-pedido').click(function(e){
		e.preventDefault();
		
		$("#input-op").attr("value", ACTION_GUARDAR_PEDIDO);
		//console.log($("#input-op"));
		
		$("form").submit();
		
		
	});
	
	
	 
	
	
	
	
	
	
	
	$('.borrar-item').click(function(e){
		e.preventDefault();
		
		console.log($(e.target).data("idx"));
		
		var idx = $(e.target).data("idx");
		
		/**
		 * El controlador recibira un indice de [0,n] e interpretara que debe borrar un elemento.
		 */
		$("#input-op").attr("value", idx);
		
		//enviar formulario:
		$("form").submit();
		
		
	});
			
	/*		
			$("#btnGetProductos").click(function(){
				var prodContanier = $("#productos-container");
				
				//console.log("sdsds");
				var url = "http://curso.kaitzen-solutions.com:3000/product";												
				
				$.get(url, [], function(response){
					console.log(response);
				});
				
				$.ajax({
					url: url ,
					beforeSend: function(xhr){xhr.setRequestHeader('auth', 'emiliano.sangoi@gmail.com');},
					success: function(response){
						
						//console.log(productos);
						var productos = response.data;
						prodContanier.text((JSON.stringify(response.data)));
						//var productos = $.parseJSON(response.data);
					},
					dataType: "json"					
				});
				
				
			});
			
			
		*/	
		});
	
