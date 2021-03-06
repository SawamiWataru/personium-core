/**
 * personium.io
 * Copyright 2014-2018 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.personium.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.personium.common.utils.PersoniumCoreUtils;
import io.personium.core.utils.UriUtils;

/**
 * 設定情報を保持するクラス. このクラスからクラスパス上にある personium-unit-config.propertiesの内容にアクセスできます。
 */
public class PersoniumUnitConfig {
    private static final int DEFAULT_BATCH_TIMEOUT = 270000;
    private static final int DEFAULT_BATCH_SLEEP_INTERVAL = 1000;
    private static final int DEFAULT_BATCH_SLEEP = 50;

    /** personium-unit-config.propertiesの設定ファイルパスキー. */
    static final String KEY_CONFIG_FILE = "io.personium.configurationFile";

    /** 本アプリで使うプロパティキーのプレフィクス. */
    static final String KEY_ROOT = "io.personium.core.";

    /** Core version設定のキー. */
    public static final String CORE_VERSION = KEY_ROOT + "version";

    /** Thread pool num io cell key. */
    public static final String THREAD_POOL_NUM_IO_CELL = KEY_ROOT + "thread.pool.num.io.cell";
    /** Thread pool num io box key. */
    public static final String THREAD_POOL_NUM_IO_BOX = KEY_ROOT + "thread.pool.num.io.box";
    /** Thread pool num misc key. */
    public static final String THREAD_POOL_NUM_MISC = KEY_ROOT + "thread.pool.num.misc";

    /** マスタートークン設定のキー. */
    public static final String MASTER_TOKEN = KEY_ROOT + "masterToken";

    /** ユニットユーザトークン発行者として認定するホスト名. */
    public static final String UNIT_USER_ISSUERS = KEY_ROOT + "unitUser.issuers";

    /** ユニットのスキーム設定キー. */
    public static final String UNIT_SCHEME = KEY_ROOT + "unitScheme";

    /** Port number key for UnitUrl. */
    public static final String UNIT_PORT = KEY_ROOT + "unitPort";

    /** Path key for UnitUrl. */
    public static final String UNIT_PATH = KEY_ROOT + "unitPath";

    /** プラグインのパス設定キー. */
    public static final String PLUGIN_PATH = KEY_ROOT + "plugin.path";

    /**
     * OData廻りの設定.
     */
    public static final class OData {
        /** $batch処理を行う際のリクエスト最大件数. */
        public static final String BATCH_BULK_REQUEST_MAX_SIZE = KEY_ROOT + "odata.batch.bulkRequestMaxSize";

        /** $batch処理のタイムアウト時間. */
        public static final String BATCH_REQUEST_TIMEOUT_IN_MILLIS = KEY_ROOT + "odata.batch.timeoutInMillis";

        /** $batch処理のスリープ時間. */
        public static final String BATCH_SLEEP_IN_MILLIS = KEY_ROOT + "odata.batch.sleepInMillis";

        /** $batch処理のスリープ間隔. */
        public static final String BATCH_SLEEP_INTERVAL_IN_MILLIS = KEY_ROOT + "odata.batch.sleepIntervalInMillis";

        /** N:Nの$linksが作成可能な最大件数. */
        public static final String NN_LINKS_MAX_NUM = KEY_ROOT + "odata.links.NtoN.maxnum";

        /** $expand指定時の$top最大数. */
        public static final String EXPAND_TOP_MAXNUM = KEY_ROOT + "odata.query.expand.top.maxnum";

        /** $expandの最大展開数（一覧取得時）. */
        public static final String EXPAND_LIST_MAXNUM = KEY_ROOT + "odata.expand.list.maxnum";

        /** $expandの最大展開数（一件取得時）. */
        public static final String EXPAND_RETRIEVE_MAXNUM = KEY_ROOT + "odata.expand.retrieve.maxnum";

        /** $topの最大値数. */
        public static final String TOP_MAX_NUM = KEY_ROOT + "odata.query.top.maxnum";

        /** $skipの最大値数. */
        public static final String SKIP_MAX_NUM = KEY_ROOT + "odata.query.skip.maxnum";

        /** 一覧取得時のデフォルト返却件数. */
        public static final String TOP_DEFAULT_NUM = KEY_ROOT + "odata.query.top.defaultnum";

        /** $expandのプロパティの最大値数（一覧取得時）. */
        public static final String EXPAND_PROPERTY_MAX_NUM_LIST = KEY_ROOT + "odata.query.expand.property.maxnum.list";

        /** $expandのプロパティの最大値数（一件取得時）. */
        public static final String EXPAND_PROPERTY_MAX_NUM_RETRIEVE = KEY_ROOT
                + "odata.query.expand.property.maxnum.retrieve";
    }

    /**
     * Account廻りの設定.
     */
    public static final class Account {
        /** パスワード認証成功時に、Accountの最終ログイン時刻を更新するか否か(true:更新する(デフォルト) false:更新しない). */
        public static final String ACCOUNT_LAST_AUTHENTICATED_ENABLED = KEY_ROOT + "account.lastauthenticated.enabled";
    }

    /**
     * Dav廻りの設定.
     */
    public static final class Dav {
        /** コレクションの子要素の最大数. */
        public static final String COLLECTION_CHILDRESOURCE_MAX_NUM = KEY_ROOT + "dav.childresource.maxnum";

        /** コレクションの階層の深さの最大数. */
        public static final String COLLECTION_DEPTH_MAX_NUM = KEY_ROOT + "dav.depth.maxnum";
    }

    /**
     * Security廻りの設定.
     */
    public static final class Security {
        /** トークンを暗号化する際に利用している秘密鍵. */
        public static final String TOKEN_SECRET_KEY = KEY_ROOT + "security.secret16";

        /** トークンを暗号化する際に利用している秘密鍵. */
        public static final String AUTH_PASSWORD_SALT = KEY_ROOT + "security.auth.password.salt";

        /** Encrypt the DAV file (true: enabled false: disabled (default)). */
        public static final String DAV_ENCRYPT_ENABLED = KEY_ROOT + "security.dav.encrypt.enabled";
    }

    /**
     * Lockの設定.
     */
    public static final class Lock {
        /** Lockのタイプ. 許容値： memcached */
        public static final String TYPE = KEY_ROOT + "lock.type";

        /** アカウントロックの有効期限(s). */
        public static final String ACCOUNTLOCK_LIFETIME = KEY_ROOT + "lock.accountlock.time";

        /** ロック取得時のリトライ回数. */
        public static final String RETRY_TIMES = KEY_ROOT + "lock.retry.times";

        /** ロック取得リトライ時の間隔. */
        public static final String RETRY_INTERVAL = KEY_ROOT + "lock.retry.interval";

        /** セルロック取得時のリトライ回数. */
        public static final String CELL_RETRY_TIMES = KEY_ROOT + "lock.cell.retry.times";

        /** セルロック取得リトライ時の間隔. */
        public static final String CELL_RETRY_INTERVAL = KEY_ROOT + "lock.cell.retry.interval";

        /** ロックをmemcachedに保持する際のmemcachedホスト名. */
        public static final String MEMCACHED_HOST = KEY_ROOT + "lock.memcached.host";

        /** ロックをmemcachedに保持する際のmemcachedポート番号. */
        public static final String MEMCACHED_PORT = KEY_ROOT + "lock.memcached.port";

        /** ロック用memcached operationタイムアウト値(ms). */
        public static final String MEMCACHED_OPTIMEOUT = KEY_ROOT + "lock.memcached.opTimeout";
    }

    /**
     * Elastic Search 関連の設定.
     */
    public static final class ES {
        /** Elastic Search ホスト設定のプロパティキー. */
        public static final String HOSTS = KEY_ROOT + "es.hosts";

        /** Elastic Search クラスタ名設定のプロパティキー. */
        public static final String CLUSTERNAME = KEY_ROOT + "es.cluster.name";

        /** Elastic Search を使用する際、DB生成時に用いるDB命名に用いるUNIT名に対応したプレフィクス設定のプロパティキー. */
        public static final String UNIT_PREFIX = KEY_ROOT + "es.unitPrefix";

        /** Elastic Search の検索結果出力上限設定のプロパティキー. */
        public static final String TOP_NUM = KEY_ROOT + "es.topnum";

        /** エラー発生時のリトライ回数. */
        public static final String RETRY_TIMES = KEY_ROOT + "es.retryTimes";

        /** エラー発生時のリトライ間隔. */
        public static final String RETRY_INTERVAL = KEY_ROOT + "es.retryInterval";
    }

    /**
     * BinaryDataの設定.
     */
    public static final class BinaryData {
        /** ファイル削除時に物理削除するかどうかの設定.(true: 物理削除, false: 論理削除). */
        public static final String PHYSICAL_DELETE_MODE = KEY_ROOT + "binaryData.physical.delete.mode";

        /** ファイルへの書き込み時にfsyncを有効にするか否か(true:有効 false:無効(デフォルト)). */
        public static final String FSYNC_ENABLED = KEY_ROOT + "binaryData.fsync.enabled";

        /** Davファイルの読み書き時、ハードリンク作成/ファイル名改変時の最大リトライ回数. */
        public static final String MAX_RETRY_COUNT = KEY_ROOT + "binaryData.dav.retry.count";

        /** Davファイルの読み書き時、ハードリンク作成/ファイル名改変時のリトライ間隔(msec). */
        public static final String RETRY_INTERVAL = KEY_ROOT + "binaryData.dav.retry.interval";
    }

    /**
     * Blobの設定.
     */
    public static final class BlobStore {
        /** Elastic Search を使用する際、blobデータを格納するルート(URL, PATH)設定のプロパティキー. */
        public static final String ROOT = KEY_ROOT + "blobStore.root";
    }

    /**
     * ユーザデータ内のプロパティの数、階層の制限設定.
     */
    public static final class UserDataProperties {
        /** EntityTypeの最大数制限. */
        public static final String MAX_ENTITY_TYPES = KEY_ROOT + "box.odata.schema.MaxEntityTypes";

        /** EntityTypeに含まれるプロパティの最大数. */
        public static final String MAX_PROPERTY_COUNT_IN_ENTITY = KEY_ROOT + "box.odata.schema.MaxProperties";

        /** 各階層のSimpleTypeの制限数 (e.g. 400,100,20,0). */
        public static final String SIMPLE_TYPE_PROPERTY_LIMITS =
                KEY_ROOT + "box.odata.schema.property.LayerLimits.SimpleType";

        /** 各階層のComplexTypeの制限数 (e.g. 20,20,50,0). */
        public static final String COMPLEX_TYPE_PROPERTY_LIMITS =
                KEY_ROOT + "box.odata.schema.property.LayerLimits.ComplexType";
    }

    /**
     * イベントの設定.
     */
    public static final class Event {
        /** 最新のイベントログファイルの格納ディレクトリ. */
        public static final String EVENT_LOG_CURRENT_DIR = KEY_ROOT + "event.log.current.dir";
        /** Maximum event hop count. */
        public static final String EVENT_HOP_MAXNUM = KEY_ROOT + "event.hop.maxnum";
    }

    /**
     * Cache廻りの設定.
     */
    public static final class Cache {
        /** Cacheのタイプ. 許容値： none/memcached */
        public static final String TYPE = KEY_ROOT + "cache.type";

        /** セルのキャッシュを有効とするか否か. 許容値： true/false */
        public static final String CELL_CACHE_ENABLED = KEY_ROOT + "cache.cell.enabled";

        /** ボックスのキャッシュを有効とするか否か. 許容値： true/false */
        public static final String BOX_CACHE_ENABLED = KEY_ROOT + "cache.box.enabled";

        /** スキーマのキャッシュを有効とするか否か. 許容値： true/false */
        public static final String SCHEMA_CACHE_ENABLED = KEY_ROOT + "cache.schema.enabled";

        /** memcachedホスト名. */
        public static final String MEMCACHED_HOST = KEY_ROOT + "cache.memcached.host";

        /** memcachedポート番号. */
        public static final String MEMCACHED_PORT = KEY_ROOT + "cache.memcached.port";

        /** memcached operationタイムアウト値(ms). */
        public static final String MEMCACHED_OPTIMEOUT = KEY_ROOT + "cache.memcached.opTimeout";

        /** キャッシュ有効期限. */
        public static final String MEMCACHED_EXPIRES_IN = KEY_ROOT + "cache.memcached.expiresin";
    }

    /**
     * Engine廻りの設定.
     */
    public static final class Engine {
        /** EngineのHostキー. */
        public static final String HOST = KEY_ROOT + "engine.host";

        /** EngineのPortキー. */
        public static final String PORT = KEY_ROOT + "engine.port";

        /** EngineのPathキー. */
        public static final String PATH = KEY_ROOT + "engine.path";
    }

    /**
     * X509廻りの設定.
     */
    public static final class X509 {
        /** X509ルート証明書を配置したパス設定のプロパティキー. */
        public static final String ROOT_CRT = KEY_ROOT + "x509.root";

        /** X509証明書を配置したパス設定のプロパティキー. */
        public static final String CRT = KEY_ROOT + "x509.crt";

        /** X509秘密鍵を配置したパス設定のプロパティキー. */
        public static final String KEY = KEY_ROOT + "x509.key";
    }

    /**
     * bar file export/install関連の設定.
     */
    public static final class BAR {
        /** barファイルの最大ファイルサイズ(MB)のプロパティキー. */
        public static final String BAR_FILE_MAX_SIZE = KEY_ROOT + "bar.file.maxSize";

        /** barファイル内エントリの最大ファイルサイズ(MB)のプロパティキー. */
        public static final String BAR_ENTRY_MAX_SIZE = KEY_ROOT + "bar.entry.maxSize";

        /** ユーザデータのリンク処理時にレスポンスを返却するサイズのプロパティキー. */
        public static final String BAR_USERDATA_LINKS_OUTPUT_STREAM_SIZE = KEY_ROOT
                + "bar.userdata.linksOutputStreamSize";

        /** ユーザデータの一括登録件数のプロパティキー. */
        public static final String BAR_USERDATA_BULK_SIZE = KEY_ROOT + "bar.userdata.bulkSize";

        /** Temporary storage directory for bar files. */
        public static final String BAR_TMP_DIR = KEY_ROOT + "bar.tmp.dir";

        /** memcachedに格納するbarインストール処理状況の有効期限（秒). */
        public static final String BAR_PROGRESS_EXPIRE_IN_SEC = KEY_ROOT + "bar.progress.expireInSec";
    }

    /**
     * cell snapshot configurations.
     */
    public static final class CellSnapshot {
        /** Root directory path to store the cell export file. */
        public static final String ROOT = KEY_ROOT + "cellSnapshot.root";
    }

    /**
     * EventBus configurations.
     */
    public static final class EventBus {
        /** message queue implementation to use. */
        public static final String MQ = KEY_ROOT + "eventbus.mq";

        /** ActiveMQ broker url. */
        public static final String ACTIVEMQ_BROKER_URL = KEY_ROOT + "eventbus.activemq.brokerUrl";

        /** Kafka servers. */
        public static final String KAFKA_SERVERS = KEY_ROOT + "eventbus.kafka.bootstrap.servers";

        /** queue name of EventBus. */
        public static final String QUEUE = KEY_ROOT + "eventbus.queue";

        /** topic name for all event. */
        public static final String TOPIC_ALL = KEY_ROOT + "eventbus.topic.all";

        /** topic name for rule event. */
        public static final String TOPIC_RULE = KEY_ROOT + "eventbus.topic.rule";

        /** Number of threads to process event. */
        public static final String EVENTPROC_THREAD_NUM = KEY_ROOT + "eventbus.eventProcessing.thread.num";
    }

    /**
     * rule configurations.
     */
    public static final class Rule {
        /** Number of threads to manage timer event. */
        public static final String TIMEREVENT_THREAD_NUM = KEY_ROOT + "rule.timerEvent.thread.num";
    }

    static {
        // 各種メッセージ出力クラスを強制的にロードする
        PersoniumCoreLog.loadConfig();
        PersoniumCoreException.loadConfig();
        PersoniumCoreAuthnException.loadConfig();
    }

    /** singleton. */
    private static PersoniumUnitConfig singleton = new PersoniumUnitConfig();

    // static Logger log = LoggerFactory.getLogger(PersoniumCoreConfig.class);

    /** 設定値を格納するプロパティ実体. */
    private final Properties props = new Properties();

    /** オーバーライドする設定値を格納するプロパティ実体. */
    private final Properties propsOverride = new Properties();

    /**
     * protectedなコンストラクタ.
     */
    protected PersoniumUnitConfig() {
        this.doReload();
    }

    /**
     * 設定のリロード.
     */
    private synchronized void doReload() {
        Logger log = LoggerFactory.getLogger(PersoniumUnitConfig.class);
        Properties properties = getUnitConfigDefaultProperties();
        Properties propertiesOverride = getPersoniumConfigProperties();
        // 読み込みに成功した場合、メンバ変数へ置換する
        if (!properties.isEmpty()) {
            this.props.clear();
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    continue;
                }
                this.props.setProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (!propertiesOverride.isEmpty()) {
            this.propsOverride.clear();
            for (Map.Entry<Object, Object> entry : propertiesOverride.entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    continue;
                }
                this.propsOverride.setProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        for (Object keyObj : propsOverride.keySet()) {
            String key = (String) keyObj;
            String value = this.propsOverride.getProperty(key);
            if (value == null) {
                continue;
            }
            log.debug("Overriding Config " + key + "=" + value);
            this.props.setProperty(key, value);
        }
    }

    private static boolean isSpaceSeparatedValueIncluded(String spaceSeparatedValue, String testValue, String unitUrl) {
        if (testValue == null || spaceSeparatedValue == null) {
            return false;
        }
        String[] values = spaceSeparatedValue.split(" ");
        for (String val : values) {
            // Correspondence when "localunit" is set for issuers.
            String convertedValue = UriUtils.convertSchemeFromLocalUnitToHttp(unitUrl, val);
            if (testValue.equals(convertedValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * personium-unit-config-default.propertiesファイルを読み込む.
     * @return personium-unit-config-default.properties
     */
    protected Properties getUnitConfigDefaultProperties() {
        Properties properties = new Properties();
        InputStream is = PersoniumUnitConfig.class.getClassLoader().getResourceAsStream(
                "personium-unit-config-default.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("failed to load config!", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("failed to close config stream", e);
            }
        }
        return properties;
    }

    /**
     * personium-unit-config.propertiesファイルを読み込む.
     * @return personium-unit-config.properties
     */
    protected Properties getPersoniumConfigProperties() {
        Logger log = LoggerFactory.getLogger(PersoniumUnitConfig.class);
        Properties propertiesOverride = new Properties();
        String configFilePath = System.getProperty(KEY_CONFIG_FILE);
        InputStream is = getConfigFileInputStream(configFilePath);
        try {
            if (is != null) {
                propertiesOverride.load(is);
            } else {
                log.debug("[personium-unit-config.properties] file not found on the classpath. using default config.");
            }
        } catch (IOException e) {
            log.debug("IO Exception when loading [personium-unit-config.properties] file.");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.debug("IO Exception when closing [personium-unit-config.properties] file.");
            }
        }
        return propertiesOverride;
    }

    /**
     * personium-unit-config.propertiesをInputStream形式で取得する.
     * @param configFilePath 設定ファイルパス
     * @return personium-unit-config.properties
     */
    protected InputStream getConfigFileInputStream(String configFilePath) {
        Logger log = LoggerFactory.getLogger(PersoniumUnitConfig.class);
        InputStream configFileInputStream = null;
        if (configFilePath == null) {
            configFileInputStream = PersoniumUnitConfig.class.getClassLoader().getResourceAsStream(
                    "personium-unit-config.properties");
            return configFileInputStream;
        }

        try {
            // 設定ファイルを指定されたパスから読み込む
            File configFile = new File(configFilePath);
            configFileInputStream = new FileInputStream(configFile);
            log.info("personium-unit-config.properties from system properties.");
        } catch (FileNotFoundException e) {
            // 指定されたパスにファイルが存在しない場合は、クラスパス上のファイルを読み込む
            configFileInputStream = PersoniumUnitConfig.class.getClassLoader().getResourceAsStream(
                    "personium-unit-config.properties");
            log.info("personium-unit-config.properties from class path.");
        }
        return configFileInputStream;
    }

    /**
     * 設定値の取得.
     * @param key キー
     * @return 設定値
     */
    private String doGet(final String key) {
        return props.getProperty(key);
    }

    /**
     * 設定値の設定.
     * @param key キー
     * @param value 値
     */
    private void doSet(final String key, final String value) {
        props.setProperty(key, value);
    }

    /**
     * すべてのプロパティを取得します。
     * @return プロパティ一覧オブジェクト
     */
    public static Properties getProperties() {
        return singleton.props;
    }

    /**
     * Key文字列を指定して設定情報を取得します.
     * @param key 設定キー
     * @return 設定値
     */
    public static String get(final String key) {
        return singleton.doGet(key);
    }

    /**
     * Key文字列を指定して設定情報を変更します.
     * @param key 設定キー
     * @param value 値
     */
    public static void set(final String key, final String value) {
        singleton.doSet(key, value);
    }

    /**
     * Core Versionの値を取得します.
     * @return Core Versionの値
     */
    public static String getCoreVersion() {
        return get(CORE_VERSION);
    }

    /**
     * Get thread pool num for cell io.
     * @return thread pool num
     */
    public static int getThreadPoolNumForCellIO() {
        return Integer.parseInt(get(THREAD_POOL_NUM_IO_CELL));
    }

    /**
     * Get thread pool num for box io.
     * @return thread pool num
     */
    public static int getThreadPoolNumForBoxIO() {
        return Integer.parseInt(get(THREAD_POOL_NUM_IO_BOX));
    }

    /**
     * Get thread pool num for misc.
     * @return thread pool num
     */
    public static int getThreadPoolNumForMisc() {
        return Integer.parseInt(get(THREAD_POOL_NUM_MISC));
    }

    /**
     * ユニットマスタートークンの値を取得します.
     * @return マスタートークンの値
     */
    public static String getMasterToken() {
        return get(MASTER_TOKEN);
    }

    /**
     * @return ユニットユーザトークン発行者として認定するホスト名.
     */
    public static String getUnitUserIssuers() {
        return get(UNIT_USER_ISSUERS);
    }

    /**
     * @return ユニットのスキーム設定キー.
     */
    public static String getUnitScheme() {
        return get(UNIT_SCHEME);
    }

    /**
     * Get port number for Unit.
     * @return port
     */
    public static int getUnitPort() {
        int port;
        try {
            port = Integer.parseInt(get(UNIT_PORT));
        } catch (NumberFormatException e) {
            port = -1;
        }
        return port;
    }

    /**
     * Get path for Unit.
     * @return path
     */
    public static String getUnitPath() {
        return get(UNIT_PATH);
    }

    /**
     * @return プラグインのパス設定キー.
     */
    public static String getPluginPath() {
        return get(PLUGIN_PATH);
    }

    /**
     * Get base url of this unit.
     * @return base url
     */
    public static String getBaseUrl() {
        String path = getUnitPath();
        if (path != null) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        } else {
            path = "";
        }

        UriBuilder uriBuilder = UriBuilder
                .fromPath(path)
                .scheme(getUnitScheme())
                .host(PersoniumCoreUtils.getFQDN())
                .port(getUnitPort());

        return uriBuilder.build().toString() + "/";
    }

    /**
     * @return $batch処理を行う際のリクエスト最大件数.
     */
    public static String getOdataBatchBulkRequestMaxSize() {

        String cnt = get(OData.BATCH_BULK_REQUEST_MAX_SIZE);
        if (cnt == null) {
            cnt = "1000";
        }
        return cnt;
    }

    /**
     * @return $batch処理のタイムアウト時間（ミリ秒）
     */
    public static long getOdataBatchRequestTimeoutInMillis() {

        String mSecInStr = get(OData.BATCH_REQUEST_TIMEOUT_IN_MILLIS);
        if (null != mSecInStr && !mSecInStr.isEmpty()) {
            return Long.parseLong(mSecInStr);
        }
        return DEFAULT_BATCH_TIMEOUT;
    }

    /**
     * @return $batch処理のスリープ時間（ミリ秒）
     */
    public static long getOdataBatchSleepInMillis() {

        String mSecInStr = get(OData.BATCH_SLEEP_IN_MILLIS);
        if (null != mSecInStr && !mSecInStr.isEmpty()) {
            return Long.parseLong(mSecInStr);
        }
        return DEFAULT_BATCH_SLEEP;
    }

    /**
     * @return $batch処理のスリープ間隔（ミリ秒）
     */
    public static long getOdataBatchSleepIntervalInMillis() {

        String mSecInStr = get(OData.BATCH_SLEEP_INTERVAL_IN_MILLIS);
        if (null != mSecInStr && !mSecInStr.isEmpty()) {
            return Long.parseLong(mSecInStr);
        }
        return DEFAULT_BATCH_SLEEP_INTERVAL;
    }

    /**
     * コレクションの子要素の最大制限数を取得.
     * @return コレクションの子要素の最大数
     */
    public static int getMaxChildResourceCount() {
        return Integer.parseInt(get(Dav.COLLECTION_CHILDRESOURCE_MAX_NUM));
    }

    /**
     * コレクションの階層の最大制限数を取得.
     * @return コレクションの階層の最大数
     */
    public static int getMaxCollectionDepth() {
        return Integer.parseInt(get(Dav.COLLECTION_DEPTH_MAX_NUM));
    }

    /**
     * ファイル書き込み時にfsyncが有効であるか否かを返す.
     * @return 有効である場合はtrue
     */
    public static boolean getFsyncEnabled() {
        return Boolean.parseBoolean(get(BinaryData.FSYNC_ENABLED));
    }

    /**
     * @return N:Nの$linksが作成可能な最大件数を取得.
     */
    public static int getLinksNtoNMaxSize() {
        return Integer.parseInt(get(OData.NN_LINKS_MAX_NUM));
    }

    /**
     * @return $expand指定時の$top最大数.
     */
    public static int getTopQueryMaxSizeWithExpand() {
        return Integer.parseInt(get(OData.EXPAND_TOP_MAXNUM));
    }

    /**
     * @return $expandの最大展開数（一覧取得時）.
     */
    public static int getMaxExpandSizeForList() {
        return Integer.parseInt(get(OData.EXPAND_LIST_MAXNUM));
    }

    /**
     * @return $expandの最大展開数（一件取得時）.
     */
    public static int getMaxExpandSizeForRetrive() {
        return Integer.parseInt(get(OData.EXPAND_RETRIEVE_MAXNUM));
    }

    /**
     * @return $topに指定可能な最大値.
     */
    public static int getTopQueryMaxSize() {
        return Integer.parseInt(get(OData.TOP_MAX_NUM));
    }

    /**
     * @return $skipに指定可能な最大値.
     */
    public static int getSkipQueryMaxSize() {
        return Integer.parseInt(get(OData.SKIP_MAX_NUM));
    }

    /**
     * @return 一覧取得時のデフォルト返却件数.
     */
    public static int getTopQueryDefaultSize() {
        return Integer.parseInt(get(OData.TOP_DEFAULT_NUM));
    }

    /**
     * @return $expandのプロパティの最大値数（一覧取得時）.
     */
    public static int getExpandPropertyMaxSizeForList() {
        return Integer.parseInt(get(OData.EXPAND_PROPERTY_MAX_NUM_LIST));
    }

    /**
     * @return $expandのプロパティの最大値数（一件取得時）.
     */
    public static int getExpandPropertyMaxSizeForRetrieve() {
        return Integer.parseInt(get(OData.EXPAND_PROPERTY_MAX_NUM_RETRIEVE));
    }

    /**
     * @return Lockのタイプ.
     */
    public static String getLockType() {
        return get(Lock.TYPE);
    }

    /**
     * @return アカウントロックの有効期限(s).
     */
    public static String getAccountLockLifetime() {
        return get(Lock.ACCOUNTLOCK_LIFETIME);
    }

    /**
     * @return ロック取得時のリトライ回数.
     */
    public static String getLockRetryTimes() {
        return get(Lock.RETRY_TIMES);
    }

    /**
     * @return ロック取得リトライ時の間隔.
     */
    public static String getLockRetryInterval() {
        return get(Lock.RETRY_INTERVAL);
    }

    /**
     * @return セルロック取得時のリトライ回数.
     */
    public static int getCellLockRetryTimes() {
        return Integer.parseInt(get(Lock.CELL_RETRY_TIMES));
    }

    /**
     * @return セルロック取得リトライ時の間隔.
     */
    public static long getCellLockRetryInterval() {
        return Long.parseLong(get(Lock.CELL_RETRY_INTERVAL));
    }

    /**
     * @return ロックをmemcachedに保持する際のmemcachedホスト名.
     */
    public static String getLockMemcachedHost() {
        return get(Lock.MEMCACHED_HOST);
    }

    /**
     * @return ロックをmemcachedに保持する際のmemcachedポート番号.
     */
    public static String getLockMemcachedPort() {
        return get(Lock.MEMCACHED_PORT);
    }

    /**
     * @return ロック用memcached operationタイムアウト値(ms).
     */
    public static long getLockMemcachedOpTimeout() {
        return Long.parseLong(get(Lock.MEMCACHED_OPTIMEOUT));
    }

    /**
     * @return 最新のイベントログファイルの格納ディレクトリ.
     */
    public static String getEventLogCurrentDir() {
        return get(Event.EVENT_LOG_CURRENT_DIR);
    }

    /**
     * Get event hop maximum number.
     * @return event hop maximum number
     */
    public static int getMaxEventHop() {
        return Integer.parseInt(get(Event.EVENT_HOP_MAXNUM));
    }

    /**
     * ElasticSearchのホスト名の設定値を取得します.
     * @return 設定値
     */
    public static String getEsHosts() {
        return get(ES.HOSTS);
    }

    /**
     * ElasticSearchのクラスタ名の設定値を取得します.
     * @return 設定値
     */
    public static String getEsClusterName() {
        return get(ES.CLUSTERNAME);
    }

    /**
     * 本アプリが担当するUnitのElasticSearch上のindex名接頭辞を取得します. 例えばu0と設定すると、管理情報indexとしてu0_adwoという名前のものを使います。
     * @return 設定値
     */
    public static String getEsUnitPrefix() {
        return get(ES.UNIT_PREFIX);
    }

    /**
     * @return Esの検索結果出力上限の設定値を取得します.
     */
    public static int getEsTopNum() {
        return Integer.parseInt(get(ES.TOP_NUM));
    }

    /**
     * @return blobデータを格納するルート(URL, PATH).
     */
    public static String getBlobStoreRoot() {
        return get(BlobStore.ROOT);
    }

    /**
     * Get root directory path to store cell snapshot file.
     * @return root directory path to store cell snapshot file
     */
    public static String getCellSnapshotRoot() {
        return get(CellSnapshot.ROOT);
    }

    /**
     * バイナリデータ(Dav/Eventlog)削除時に物理削除するかどうかの設定.
     * @return true: 物理削除, false: 論理削除
     */
    public static boolean getPhysicalDeleteMode() {
        return Boolean.parseBoolean(get(BinaryData.PHYSICAL_DELETE_MODE));
    }

    /**
     * Davファイルのハードリンク作成/改名/削除時のリトライ回数.
     * @return リトライ回数
     */
    public static int getDavFileOperationRetryCount() {
        return Integer.parseInt(get(BinaryData.MAX_RETRY_COUNT));
    }

    /**
     * Davファイルのハードリンク作成/改名/削除時のリトライ間隔(msec).
     * @return リトライ間隔(msec)
     */
    public static long getDavFileOperationRetryInterval() {
        return Long.parseLong(get(BinaryData.RETRY_INTERVAL));
    }

    /**
     * @return ESでエラー発生時のリトライ回数.
     */
    public static String getESRetryTimes() {
        return get(ES.RETRY_TIMES);
    }

    /**
     * @return ESでエラー発生時のリトライ間隔(ミリ秒).
     */
    public static String getESRetryInterval() {
        return get(ES.RETRY_INTERVAL);
    }

    /**
     * Get max size of bar file.
     * @return max size of bar file
     */
    public static long getBarEntryMaxSize() {
        return Long.parseLong(get(BAR.BAR_ENTRY_MAX_SIZE));
    }

    /**
     * Get temp dir path for barinstall.
     * @return temp dir path
     */
    public static String getBarInstallTempDir() {
        return get(BAR.BAR_TMP_DIR) + "/install";
    }

    /**
     * Get temp dir path for barexport.
     * @return temp dir path
     */
    public static String getBarExportTempDir() {
        return get(BAR.BAR_TMP_DIR) + "/export";
    }

    /**
     * @return barインストールの非同期処理状況の有効期限(s).
     */
    public static String getBarInstallProgressLifeTimeExpireInSec() {
        return get(BAR.BAR_PROGRESS_EXPIRE_IN_SEC);
    }

    /**
     * @return Cacheのタイプ.
     */
    public static String getCacheType() {
        return get(Cache.TYPE);
    }

    /**
     * @return memcachedホスト名.
     */
    public static String getCacheMemcachedHost() {
        return get(Cache.MEMCACHED_HOST);
    }

    /**
     * @return memcachedポート番号.
     */
    public static String getCacheMemcachedPort() {
        return get(Cache.MEMCACHED_PORT);
    }

    /**
     * @return cache用memcached operationタイムアウト値(ms).
     */
    public static long getCacheMemcachedOpTimeout() {
        return Long.parseLong(get(Cache.MEMCACHED_OPTIMEOUT));
    }

    /**
     * Cellのキャッシュが有効か否かを返す.
     * @return 有効な場合はtrue.
     */
    public static boolean isCellCacheEnabled() {
        return Boolean.parseBoolean(get(Cache.CELL_CACHE_ENABLED));
    }

    /**
     * Boxのキャッシュが有効か否かを返す.
     * @return 有効な場合はtrue.
     */
    public static boolean isBoxCacheEnabled() {
        return Boolean.parseBoolean(get(Cache.BOX_CACHE_ENABLED));
    }

    /**
     * スキーマのキャッシュが有効か否かを返す.
     * @return 有効な場合はtrue.
     */
    public static boolean isSchemaCacheEnabled() {
        return Boolean.parseBoolean(get(Cache.SCHEMA_CACHE_ENABLED));
    }

    /**
     * @return memcachedキャッシュ有効期限.
     */
    public static int getCacheMemcachedExpiresIn() {
        return Integer.parseInt(get(Cache.MEMCACHED_EXPIRES_IN));
    }

    /**
     * Enineのホスト名設定値を取得します.
     * @return 設定値
     */
    public static String getEngineHost() {
        return get(Engine.HOST);
    }

    /**
     * Enineのportの設定値を取得します.
     * @return 設定値
     */
    public static int getEnginePort() {
        return Integer.parseInt(get(Engine.PORT));
    }

    /**
     * Enineののパスの設定値を取得します.
     * @return 設定値
     */
    public static String getEnginePath() {
        return get(Engine.PATH);
    }

    /**
     * 本UNITのX509秘密鍵ファイルのパスの設定値を取得します.
     * @return 設定値
     */
    public static String getX509PrivateKey() {
        return get(X509.KEY);
    }

    /**
     * 本UNITのX509ルート証明書ファイルのパスの設定値の配列を取得します.
     * @return 設定値
     */
    public static String[] getX509RootCertificate() {
        String[] x509RootCertificate = null;
        String value = get(X509.ROOT_CRT);
        if (value != null) {
            x509RootCertificate = value.split(" ");
        }
        return x509RootCertificate;
    }

    /**
     * 本UNITのX509証明書ファイルのパスの設定値を取得します.
     * @return 設定値
     */
    public static String getX509Certificate() {
        return get(X509.CRT);
    }

    /**
     * トークンを暗号化する際に利用している秘密鍵設定.
     * @return 設定値
     */
    public static String getTokenSecretKey() {
        return get(Security.TOKEN_SECRET_KEY);
    }

    /**
     * 認証時に使用するパスワードのソルト値.
     * @return パスワードのソルト値
     */
    public static String getAuthPasswordSalt() {
        return get(Security.AUTH_PASSWORD_SALT);
    }

    /**
     * Encrypt the DAV file.
     * @return true: enabled false: disabled
     */
    public static boolean isDavEncryptEnabled() {
        return Boolean.parseBoolean(get(Security.DAV_ENCRYPT_ENABLED));
    }

    /**
     * Get message queue implementation of EventBus.
     * @return message queue
     */
    public static String getEventBusMQ() {
        return get(EventBus.MQ);
    }

    /**
     * Get broker url of setting for activemq.
     * @return broker url
     */
    public static String getEventBusActiveMQBrokerUrl() {
        return get(EventBus.ACTIVEMQ_BROKER_URL);
    }

    /**
     * Get servers of setting for kafka.
     * @return comma-separated servers
     */
    public static String getEventBusKafkaServers() {
        return get(EventBus.KAFKA_SERVERS);
    }

    /**
     * Get queue name of EventBus.
     * @return queue name
     */
    public static String getEventBusQueueName() {
        return get(EventBus.QUEUE);
    }

    /**
     * Get topic name for all event.
     * @return topic name
     */
    public static String getEventBusTopicName() {
        return get(EventBus.TOPIC_ALL);
    }

    /**
     * Get topic name for rule event.
     * @return topic name
     */
    public static String getEventBusRuleTopicName() {
        return get(EventBus.TOPIC_RULE);
    }

    /**
     * Get thread number for eventprocessing.
     * @return thread num
     */
    public static int getEventProcThreadNum() {
        return Integer.parseInt(get(EventBus.EVENTPROC_THREAD_NUM));
    }

    /**
     * Get thread number of timer event.
     * @return thread num
     */
    public static int getTimerEventThreadNum() {
        return Integer.parseInt(get(Rule.TIMEREVENT_THREAD_NUM));
    }

    /**
     * 設定情報をリロードします.
     */
    public static void reload() {
        singleton.doReload();
    }

    /**
     * 実行環境がhttpsかどうかを返却します.
     * @return boolean httpsの場合:true
     */
    public static boolean isHttps() {
        return PersoniumUnitConfig.getUnitScheme().equals("https");
    }

    /**
     * Check whether the specified URL is included in the host name certified as UnitUserToken Issuer.
     * @param url Target URL
     * @param unitUrl Unit URL
     * @return Included:true
     */
    public static boolean checkUnitUserIssuers(String url, String unitUrl) {
        return isSpaceSeparatedValueIncluded(getUnitUserIssuers(), url, unitUrl);
    }

    /**
     * EntityTypeの最大制限数を取得.
     * @return EntityTypeの最大数
     */
    public static int getUserdataMaxEntityCount() {
        return Integer.parseInt(get(UserDataProperties.MAX_ENTITY_TYPES));
    }

    /**
     * EntityTypeに包含可能なプロパティの最大制限数を取得.
     * @return EntityType内の最大プロパティ数
     */
    public static int getMaxPropertyCountInEntityType() {
        return Integer.parseInt(get(UserDataProperties.MAX_PROPERTY_COUNT_IN_ENTITY));
    }

    /**
     * UsetData内のSimpleTypeの制限数の配列を取得.
     * @return 各階層における SimpleTypeの制限数のリスト
     */
    public static int[] getUserdataSimpleTypePropertyLimits() {
        String expr = get(UserDataProperties.SIMPLE_TYPE_PROPERTY_LIMITS);
        return getPropertyLimits(expr, -1);
    }

    /**
     * UsetData内のComplexTypeの制限数の配列を取得.
     * @return 各階層における SimpleTypeの制限数のリスト
     */
    public static int[] getUserdataComplexTypePropertyLimits() {
        int depth = getUserdataSimpleTypePropertyLimits().length;
        String expr = get(UserDataProperties.COMPLEX_TYPE_PROPERTY_LIMITS);
        return getPropertyLimits(expr, depth);
    }

    /**
     * パスワード認証成功時に、Accountの最終ログイン時刻を更新するか否か().
     * @return true:更新する(デフォルト) false:更新しない
     */
    public static boolean getAccountLastAuthenticatedEnable() {
        return Boolean.parseBoolean(get(Account.ACCOUNT_LAST_AUTHENTICATED_ENABLED));
    }

    /**
     * カンマ区切りで指定された数値リスト(文字列)を、int型の配列にして返す。
     * arrayLengthで指定された長さよりも、数値リストの内容が長い場合は、長い部分は捨てられる。<br/>
     * e.g. limitExpression : "1,2,3,4", arrayLength = 2 → int[] { 1, 2 }
     * arrayLengthで指定された長さよりも、数値リストの内容が短い場合は、足りない部分が 0詰めされる。<br/>
     * e.g. limitExpression : "1,2", arrayLength = 4 → int[] { 1, 2, 0, 0 }
     * @param limitExpression カンマ区切りの数値リスト(文字列)
     * @param arrayLength 返す配列の長さ。0以下の場合は、limitExpressionから得られる要素数の配列を指定したのと同じとなる。
     * @return 整数配列
     */
    private static int[] getPropertyLimits(String limitExpression, int arrayLength) {
        if (null != limitExpression) {
            String[] values = limitExpression.split(",");
            if (arrayLength < 1) {
                arrayLength = values.length;
            }
            int[] result = new int[arrayLength];
            Arrays.fill(result, 0);
            for (int i = 0; i < values.length && i < arrayLength; i++) {
                if ("*".equals(values[i])) {
                    // -1 は無制限を示す数値
                    result[i] = -1;
                } else {
                    result[i] = Integer.parseInt(values[i]);
                    // Notice: 数値で無かった場合は、NumberFormatExceptionが投げられる。
                }
            }
            return result;
        }
        return new int[] {};
    }
}
