<h1>Swiggy</h1><br>
Address and Latlong Pick From Map or Plack Picker Library

<h2>Key Features:</h2>
✔ works with any version of app and display<br>
✔ select address by placer picker, map<br>
✔ auto save recent selected address<br>
✔ view recent address<br>
✔ light weight and hassle free library<br>
✔ select address<br>
✔ save favourite address<br>
✔ edit address<br>

## Usage
Download sample for better understanding

*  <b>Select Address:-></b>
    <pre>Intent intent = new Intent(this, SelectLocationActivity.class);
        <b>intent.putExtra("action", "select");</b>
        intent.putExtra("saved_address", saved_address_array);
        startActivityForResult(intent, REQUEST_SELECT);</pre>
    
*  <b>Add Address:-></b>
    <pre>Intent intent = new Intent(this, SelectLocationActivity.class);
        <b>intent.putExtra("action", "add");</b>
        intent.putExtra("saved_address", saved_address_array);
        startActivityForResult(intent, REQUEST_ADD);</pre>
    

*  <b>Edit Address:-></b>
    <pre>Intent intent = new Intent(this, SelectLocationActivity.class);
        <b>intent.putExtra("action", "edit");</b>
        intent.putExtra("saved_address", saved_address_array);

        intent.putExtra("type", tvType.getText().toString());
        intent.putExtra("address", tvAddress.getText().toString());
        intent.putExtra("landmark", tvLandmark.getText().toString());
        intent.putExtra("house_no", tvHouseNo.getText().toString());
        intent.putExtra("lat", Double.valueOf(tvLat.getText().toString()));
        intent.putExtra("lng", Double.valueOf(tvLng.getText().toString()));
        startActivityForResult(intent, REQUEST_EDIT);</pre>
    
    
## Gradle
**Step 1.** Add the JitPack repository to your build file
    Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			
			maven { url 'https://jitpack.io' }
		}
	}
**Step 2.** Add the dependency
    
    dependencies {
	        implementation 'com.github.1211amarsingh:swiggy:v1.1'
	}


## How to Implement
<b> Step1.</b> Add Your_API_KEY In Manifest<br>
```
<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="Your_API_KEY" />
```

<b> Step2.</b> Add <b>Gradle</b>  in app.gradle<br><br>
<b> Step3.</b> Add Compile Options in app. gradle<br>
<pre>
compileOptions {
		sourceCompatibility = '1.8'
		targetCompatibility = '1.8'
}
</pre><br>
<b> Step4.</b> Follow <b>Usage </b> as required<br><br>
<b> Step5.</b> Last Step <b>@Override onActivityResult</b> Here you wil get your result
<pre> @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT) {
            if (resultCode == RESULT_OK) {
                //address selected
                UserData.Address address = (UserData.Address) data.getSerializableExtra("address");
            } else if (resultCode == RESULT_FIRST_USER) {
                //new address submited
                UserData.Address address = (UserData.Address) data.getSerializableExtra("address");
            } else {
                Toast.showToast(this, "RESULT_CANCELED");
            }
        } else if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_CANCELED) {
                Toast.showToast(this, "RESULT_CANCELED");
            } else {
                //new address submited
                UserData.Address address = (UserData.Address) data.getSerializableExtra("address");
            }

        } else if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_CANCELED) {
                Toast.showToast(this, "RESULT_CANCELED");
            } else {
                //address edited
               UserData.Address address = (UserData.Address) data.getSerializableExtra("address");
            }
        }
    }
</pre>

## Output 
![](https://github.com/1211amarsingh/swiggy/blob/master/swiggy_address_pick.gif)

## Demo
Feel free to clone this project and run in your IDE to see how can be implemented :).

## Version
Latest Version : [![](https://jitpack.io/v/1211amarsingh/swiggy.svg)](https://jitpack.io/#1211amarsingh/swiggy)
## Contributor
<b>Pramit Chaturvedi</b>   
<a href="https://www.linkedin.com/in/pramit-chaturvedi-02064147/">
		<img src="https://github.com/1211amarsingh/PrivacyPolicy/blob/master/linkedinbadge.png" style="max-width:100%;" width="30%"/></a>
    
<p align="center">
	<a href="https://play.google.com/store/apps/details?id=com.kv.swiggyaddresspick">
		<img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" style="max-width:100%;" width="30%"/></a>
</p>

<b> ▷ Other Apps: </b>
* <a href="https://play.google.com/store/apps/details?id=com.kv.swiggyaddresspick">Swiggy Address Pick</a>
* <a href="https://play.google.com/store/apps/details?id=com.kv.callrecorder">Call Recorder Free</a>
* <a href="https://play.google.com/store/apps/details?id=com.kv.popupimageviews1">Popup Image View</a>
          
<b>▷ Library:</b>

- <a href="https://github.com/1211amarsingh/swiggy">Swiggy Address Pick</a>
- <a href="https://github.com/1211amarsingh/PopupImageView">Popup Image View</a>


---------------------------------------------------------
We're always excited to hear from you! If you have any request, suggestions, feedback, questions, or concerns, please email us at:

 <a href="mailto:1211AMARSINGH@GMAIL.COM" >1211AMARSINGH@GMAIL.COM</a>
