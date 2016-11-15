[TOP](../../../../README.md)　>　[Unityプラグインの導入手順](../../README.md)　>　[Androidプロジェクトの設定](../README.md)　>　**複数のINSTALL_REFERRERレシーバーを設定**

---

## 複数のINSTALL_REFERRERレシーバーを共存させる場合の設定

"com.android.vending.INSTALL_REFERRER"に対するレシーバークラスは一つしか定義することができません。

F.O.X以外のSDK等、既に"com.android.vending.INSTALL_REFERRER"に対するレシーバークラスが定義されている場合は、F.O.X SDKが用意しているレシーバークラスから、他のレシーバークラスを呼び出すことで共存させることが可能です。

AndroidManifest.xmlを編集し、下記の設定を追加してください。

```xml
<!-- レシーバークラスはF.O.X SDKのクラスを指定します -->
<receiver
	android:name="co.cyberz.fox.FoxInstallReceiver"
	android:exported="true">
	<intent-filter>
		<action android:name="com.android.vending.INSTALL_REFERRER" />
	</intent-filter>
</receiver>

<!-- F.O.X SDKから呼び出したい他のレシーバークラス情報をmeta-dataとして記述します -->
<meta-data
		android:name="APPADFORCE_FORWARD_RECEIVER"
		android:value="com.example.InstallReceiver1|com.example.InstallReceiver2|com.example.InstallReceiver3" />
```

> `APPADFORCE_FORWARD_RECEIVER`に指定するクラスはパッケージ付きで指定してください。また、`|`(パイプ)で区切ることで複数のクラスを指定することが可能です。

> Proguardを利用する場合、`APPADFORCE_FORWARD_RECEIVER`に指定するクラスは-keep指定でクラス名が変更されないようにしてください。<br>
Proguardの対象となりますとF.O.X SDKがクラスを探せなくなり正常に動作しませんのでご注意ください。


---
[戻る](../README.md)

[TOP](../../../../README.md)
