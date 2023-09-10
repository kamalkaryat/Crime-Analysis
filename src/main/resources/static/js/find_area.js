const state= document.getElementById('state')
const district= document.getElementById('district')
const pincode= document.getElementById('pincode')
const areaName= document.getElementById('areaName')
/* Populate States */

const stateList= async()=>{
	const url= 'http://localhost:8080/area-api/states';
	const res= await fetch(url);
	let states= await res.json();
	states.sort();
	
	let tmpStates= states.toString().split(",");
	autocomplete(state, tmpStates);	
};
state.addEventListener("input", stateList);

/* Populate Districts */
const districtList= async ()=> {
	let st= state.value;
	if(st.length <= 0){
		let tmp= [];
		autocomplete(district, tmp);	
		return;
	}
	const url= 'http://localhost:8080/area-api/states/'+st;
	const res= await fetch(url);
	let districts= await res.json();
	districts.sort();
	
  	let tmpDist= districts.toString().split(",");
	autocomplete(district, tmpDist);
}
district.addEventListener("input",districtList)

/* Populate Pincodes */
const pincodeList= async ()=> {
	let st= state.value;
	let dis= district.value;
	if(st.length <= 0 && dis.length <= 0){
		let tmp= [];
		autocomplete(pincode, tmp);	
		return;
	}

	const url= 'http://localhost:8080/area-api/states/'+st+'/districts/'+dis;
	const res= await fetch(url);
	let pincodes= await res.json();	
	
	let tmpPin= pincodes.toString().split(",");
	autocomplete(pincode, tmpPin);	
}
pincode.addEventListener("input",pincodeList)

/* Populate Area Names */
const areaNameList= async ()=> {
	let pin= pincode.value;
	if(pin.length <= 0){
		let tmp= [];
		autocomplete(areaName, tmp);	
		return;
	}
	const url= 'http://localhost:8080/area-api/states/state/districts/district/'+pin;
	const res= await fetch(url);
	let areaNames= await res.json();
	areaNames.sort();
	
	let tmpAreaName= areaNames.toString().split(",");
	autocomplete(areaName, tmpAreaName);		
}
areaName.addEventListener("keydown", areaNameList);

/* Create dynamic div for auto completion */
function autocomplete(inp, arr) {
  /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
  var currentFocus;
 
  /*execute a function when someone writes in the text field:*/
  inp.addEventListener("input", function(e) {
      var a, b, i, val = this.value;
      /*close any already open lists of autocompleted values*/
      closeAllLists();
      if (!val) { return false;}
      currentFocus = -1;
      /*create a DIV element that will contain the items (values):*/
      a = document.createElement("DIV");
      a.setAttribute("id", this.id + "autocomplete-list");
      a.setAttribute("class", "autocomplete-items");
      /*append the DIV element as a child of the autocomplete container:*/
      this.parentNode.appendChild(a);
      /*for each item in the array...*/
      for (i = 0; i < arr.length; i++) {
        /*check if the item starts with the same letters as the text field value:*/
        if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
          /*create a DIV element for each matching element:*/
          b = document.createElement("DIV");
          /*make the matching letters bold:*/
          b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
          b.innerHTML += arr[i].substr(val.length);
          /*insert a input field that will hold the current array item's value:*/
          b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
          /*execute a function when someone clicks on the item value (DIV element):*/
              b.addEventListener("click", function(e) {
              /*insert the value for the autocomplete text field:*/
              inp.value = this.getElementsByTagName("input")[0].value;
              /*close the list of autocompleted values,
              (or any other open lists of autocompleted values:*/
              closeAllLists();
          });
          a.appendChild(b);
        }
      }
  });
  function addActive(x) {
    /*a function to classify an item as "active":*/
    if (!x) return false;
    /*start by removing the "active" class on all items:*/
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    /*add class "autocomplete-active":*/
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    /*a function to remove the "active" class from all autocomplete items:*/
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
      	x[i].parentNode.removeChild(x[i]);
      }
  	}
  }
  
/*execute a function when someone clicks in the document:*/
  document.addEventListener("click", function (e) {
  	closeAllLists(e.target);
  });
}
