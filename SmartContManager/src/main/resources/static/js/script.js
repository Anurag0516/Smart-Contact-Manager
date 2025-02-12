console.log("this is script file")

const toggleSidebar=()=>{
	
	if($('.sidebar').is(":visible")){
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}
	else{
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
	
};

const search=()=>{
	
	let query = $("#search-input").val();
	if(query == ""){
		$(".search-result").hide();
	}
	else{
		//sending request to server
		let url = `http://localhost:8080/search/${query}`;
		
		fetch(url)
		.then(response => {
			return response.json();
		})
		.then((data) => {
			//data...
			let text=`<div class='list-group'>`;
			
			data.forEach((contact) => {
				text += `<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'>${contact.name +' '+contact.secondName}</a>`;
			});
			
			text+=`</div>`;
			
			$(".search-result").html(text);
			$(".search-result").show();
		});
		
		$(".search-result").show();
	}
};

//first request to server to create order

const paymentStart=()=>{
	console.log("payment started..")
	let amount = $("#payment_field").val();
	console.log(amount);
	if(amount=='' || amount==null){
		alert("amount is required !!")
		return;
	}

	//code..
	//we will use AJAX to send request to server to create order
	
	$.ajax(
		{
			url:'/user/create_order',
			data:JSON.stringify({amount:amount,info:'order_request'}),
			contentType:'application/json',
			type:'POST',
			dataType:'json',
			success:function(response){
				//this function invoked when success
				console.log(response);
				if(response.status=='created'){
					//open payment Form
					let options={
						key:'rzp_test_dv8Vk7SsTQ0Tmd',
						amount:response.amount,
						currency:'INR',
						name:'Smart Contact Manager',
						descrition:'Donation',
						image:'https://learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Flcwd_logo.45da3818.png&w=640&q=75',
						order_id:response.order_id,
						handler:function(response){
							console.log(response.razorpay_payment_id)
							console.log(response.razorpay_order_id)
							console.log(response.razorpay_signature)
							console.log('Payment Successful !!')
							alert('congrats !! Payment Successfull !!')
						},
						"prefill": {
							"name": "",
							"email": "",
							"contact": ""
						},
						"notes": {
							"address": "Anurag Agarwal"
						},
						"theme": {
							"color": "#3399cc"
						},
					};

					let rzp = new Razorpay(options);

					rzp.on('payment.failed', function (response){
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);
						alert("Oops payment failed !!");
					});

					rzp.open();
				}
			},
			error:function(error){
				//invoked when error
				console.log(error)
				alert("Something went wrong !!");
			}
		}
	)

};