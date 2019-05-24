# swiggy
Address Pick From Map or Plack Picker Library

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
          ```<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="Your_API_KEY" />```<br><br>
<b> Step2.</b> Add <b>Gradle</b>  in app.gradle<br><br>
<b> Step3.</b> Add Compile Options in app. gradle<br>
          ```compileOptions {
                    sourceCompatibility = '1.8'
                    targetCompatibility = '1.8'
              }```<br><br>
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
![](url)

## Demo
Feel free to clone this project and run in your IDE to see how can be implemented :).

## Version
Latest Version : [![](https://jitpack.io/v/1211amarsingh/swiggy.svg)](https://jitpack.io/#1211amarsingh/swiggy)
## Contributor
<b>Pramit Chaturvedi</b>   
<a href="https://www.linkedin.com/in/pramit-chaturvedi-02064147/">
		<img src="https://github.com/1211amarsingh/PrivacyPolicy/blob/master/linkedinbadge.png" style="max-width:100%;" width="30%"/>
    
<p align="center">
	<a href="https://play.google.com/store/apps/details?id=com.kv.swiggyaddresspick">
		<img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" style="max-width:100%;" width="30%"/>
</p>
