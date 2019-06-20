# web3j demo 

    //以太坊 钱包,加解密的
    api('org.web3j:core:3.3.1-android') {
        exclude group: 'com.squareup.okhttp3', module: 'okhttp'
        exclude module: 'rxjava'
    }