/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  ToastAndroid
} from 'react-native';
import {NativeModules, AppState} from 'react-native';
import { StackNavigator } from "react-navigation";

import { YellowBox } from 'react-native'


const wifi = NativeModules.wifi;
const IMEI = NativeModules.IMEI;
const storage = NativeModules.storage;
const Fingerprint = NativeModules.FingerprintAndroid;
const ClearCacheModule = NativeModules.ClearCacheModule;
const ExitApp = NativeModules.ExitApp;

// type Props = {};

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      imei: '',
      ip: '',
      t_storage:'',
      f_storage:'',      
    };
  }

  componentDidMount(){
    IMEI.getIMEI((IMEI)=>{
      // ToastAndroid.show('IMEI = '+IMEI, ToastAndroid.LONG);
      this.setState({imei:IMEI});
  })  
   
    wifi.getIpAddress((ipAddress)=>{
      // ToastAndroid.show('IPADD = '+ipAddress, ToastAndroid.LONG);
      this.setState({ip:ipAddress});
    })  
    
    storage.getTotalDiskCapacity((t_storage)=>{
      // ToastAndroid.show('IPADD = '+ipAddress, ToastAndroid.LONG);
      this.setState({t_storage:t_storage});
    })  
    storage.getFreeDiskStorage((f_storage)=>{
      // ToastAndroid.show('IPADD = '+ipAddress, ToastAndroid.LONG);
      this.setState({f_storage:f_storage});
    })  
   }

  
  
  render() {
 
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!  
        </Text>
        <Text style={styles.instructions}>
           {"IMEI ="+ this.state.imei}
        </Text>
        <Text style={styles.instructions}>
          {"IP ="+ this.state.ip}
        </Text>
        <Text style={styles.instructions}>
          {"Total Storage ="+ this.state.t_storage}
        </Text>
        <Text style={styles.instructions}>
          {"Free Storage ="+ this.state.f_storage}
        </Text>
      </View>
    );
  }
}


class jari extends Component {
  constructor(props) {
    super(props);
    this.state = {
      phase: 'normal',
      message: '',
    };
  }

  async componentDidMount(){
    console.log("componentDidMount")
    YellowBox.ignoreWarnings(['Warning: isMounted(...) is deprecated'])
    await this.authenticate()
    // setTimeout(()=>this.authenticate(),500);
    await  AppState.addEventListener("change", async(state) => {
          console.log("change")
            try {
              // console.log(Fingerprint.isAuthenticationCanceled())
              
              // ToastAndroid.show("benar salah "+state === "active" && await Fingerprint.isAuthenticationCanceled(), ToastAndroid.LONG);
                if(state === "active" && await Fingerprint.isAuthenticationCanceled()) {
               
                  await this.authenticate()
                  ToastAndroid.show("masuk", ToastAndroid.LONG); 
                }
            }
            catch(z) {
                console.log(z)
                ToastAndroid.show('error addevent listener = '+z, ToastAndroid.LONG);
            }
        })
   }

   async authenticate() {
    console.log("authenticate")
    // this.setState({
    //     phase: 'normal', 
    //     message: ''
    // })
    
    try {
        // do sanity checks before starting authentication flow.
        // HIGHLY recommended in real life usage. see more on why you should do this in the readme.md
        const hardware = await Fingerprint.isHardwareDetected();
        const permission = await Fingerprint.hasPermission();
        const enrolled = await Fingerprint.hasEnrolledFingerprints();
        console.log("hardware = "+hardware)
        console.log("permission = "+permission)
        console.log("enrolled = "+enrolled)
      

        if (!hardware || !permission || !enrolled) {
            let message = !enrolled ? 'No fingerprints registered.' : !hardware ? 'This device doesn\'t support fingerprint scanning.' : 'App has no permission.'
            // this.setState({
            //     phase: 'fail',
            //     message:message
            // });
            
             ToastAndroid.show('Gagal'+message, ToastAndroid.LONG);
            return;
        }
        console.log("disini")
        await Fingerprint.authenticate().then(promise => {
          console.log("disini 1")
          // console.log(warning)
          console.log(promise)
          // ToastAndroid.show('Warn '+promise.message, ToastAndroid.LONG);
            // this.setState({
            //     phase: 'warn',
            //     // message: warning.message
            // })
        });

        // if we got this far, it means the authentication succeeded.
        this.setState({
            phase: 'success',
            message: 'Correct'
        });
        this.props.navigation.navigate("Main");
        
        ToastAndroid.show('Sukses', ToastAndroid.LONG);

        // // in real life, we'd probably do something here (process the payment, unlock the vault, whatever)
        // // but this is a demo. so restart authentication.
        setTimeout(() => this.authenticate(), 3000);
        // Fingerprint.cancelAuthentication()
        
    } catch (error) {
      console.log("error "+error.code == Fingerprint.FINGERPRINT_ERROR_CANCELED )
    console.log("error "+error.code)
     
      if(error.code == Fingerprint.FINGERPRINT_ERROR_CANCELED) {
        console.log("error masuk sini")
        // we don't show this error to the user.
            // we will check if the auth was cancelled & restart the flow when the appstate becomes active again.
            return;
        }
        this.setState({
            phase:"fail",
            message: error.message
        })
        console.log("error 2")
      
    }
}



async componentWillUnmount() {
  try {
    console.log("componentWillUnmount")
    // ToastAndroid.show('componentWillUnmount', ToastAndroid.LONG);
    // clearAppCache(callBack) {
    // }
    // this.isMounted = false;
    // AppState.removeEventListener('change');
        await Fingerprint.isAuthenticationCanceled().then(promise=>{
          // ToastAndroid.show("Fingerprint.isAuthenticationCanceled() = " +promise , ToastAndroid.LONG);

          if(!promise) {
                //stop listening to authentication.
             
                Fingerprint.cancelAuthentication().then(
                  promise=>{
                    console.log("Fingerprint.cancelAuthentication() = " +promise) 
                  });
                // this.componentDidMount.cancel();
          }
        })
        console.log("stop listening to authentication");
        
        // ToastAndroid.show('cancelAuthentication = '+Fingerprint.isAuthenticationCanceled(), ToastAndroid.LONG);
      
        ClearCacheModule.clearAppCache(()=>{
            ToastAndroid.show("Cache di Bersihkan",  ToastAndroid.LONG)
        });
        ClearCacheModule.getAppCacheSize((value, unit) => {
          ToastAndroid.show("value=" + value,  ToastAndroid.LONG)          
          ToastAndroid.show("unit=" + unit,  ToastAndroid.LONG)
       })
       ExitApp.exitApp();
  } catch(z) {
    ToastAndroid.show('componentWillUnmount error '+z, ToastAndroid.LONG);
    console.log("componentWillUnmount error")
      console.log(z);
  }
}


  render() { 
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
         FINGERPRINT APP
        </Text>
        <Text style={styles.instructions}>
           {"phase ="+ this.state.phase}
        </Text>
        <Text style={styles.instructions}>
          {this.state.message}
        </Text>
      </View>
    );
  }
}



export default  StackNavigator(
  {
    jari: {
      screen: jari,
      navigationOptions: {
        headerTitle: 'Finger Print',
      },
    },
    Main: {
      screen: App,
      navigationOptions:{ 
        headerTitle: 'APP',
      }
    },
  },
  {
    initialRouteName: "jari"
  }
);



const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});