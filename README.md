# シンプルToDoリスト
タスク内容と期日に焦点を絞り、「なにをいつまでにするか」を明確にするためのアプリです。
## Screenshots
<img src="https://user-images.githubusercontent.com/87401291/126035602-60714f79-822c-4e6a-9571-a60462a6b80f.png" width="200px"> <img src="https://user-images.githubusercontent.com/87401291/126035778-a021dc95-8014-49de-9833-f5f2de68ea5b.png" width="200px"> <img src="https://user-images.githubusercontent.com/87401291/126035810-64c186cf-5200-48c3-9e07-1340bed53541.png" width="200px">
## Features
* １つのリストでタスクを全て管理  
* 入力に必要な情報はタスク内容と期限のみ  
* リスト内タスクの検索が可能  
* リマインダーを設定することで期限を通知  
* 「タスクの作成順」「タスクの期限順」にソート  
## Architecture
* Google推奨のアーキテクチャを利用
<img src="https://user-images.githubusercontent.com/87401291/126036595-306d9406-9a38-43cc-9568-7b6523271f0f.jpeg" width="640px">
### Used libraries
* ViewModel
* LiveData
* Databinding
* Room
* Navigation
* Safe Args
* Kotlin Coroutine
* Dagger Hilt
