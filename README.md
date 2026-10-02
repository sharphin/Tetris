# Tetris
__試作のデモテトリス__ (libGDX 版)

`game/` 以下は [libGDX](https://libgdx.com/) を使ったマルチモジュール Gradle プロジェクトです。

| モジュール | 内容 |
| --- | --- |
| `core` | ゲーム本体 (画面・ロジック)。プラットフォーム非依存 |
| `lwjgl3` | デスクトップ (Windows / macOS / Linux) 用ランチャー |
| `assets` | 画像などのリソース |

*実行方法*
* `cd game && ./gradlew lwjgl3:run`

*実行可能 JAR の作成*
* `cd game && ./gradlew lwjgl3:dist` → `game/lwjgl3/build/libs/tetris-all.jar`
* `java -jar tetris-all.jar` で起動

*テスト*
* `cd game && ./gradlew test`

Java 25 のツールチェーンを使います。別のバージョンでビルドする場合は `-PjavaVersion=21` のように指定してください。

*操作*
* ← / → : 移動
* ↓ : ソフトドロップ (押している間高速落下。落下したマス数がスコアに加算)
* ↑ : ハードドロップ
* G : 右回転 / F : 左回転
* Space : ホールド (1 ミノにつき 1 回)
* Enter : ゲームオーバー / クリア後にリスタート

*ルール* (C# 版と同じ)
* 7 種 1 巡のランダム順 (7-bag)、NEXT 6 個表示、ゴースト表示
* 消去ライン数に応じて 40 / 100 / 300 / 1200 × レベル点
* 10 ライン毎にレベルアップして落下速度が上がる
* 200 ラインでゲームクリア、出現エリアまで積み上がるとゲームオーバー

*C# 版*
* Tetris.exe(C#で書き直したバージョン)をダブルクリック
