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
package io.personium.core.model;

import io.personium.core.PersoniumCoreAuthzException;
import io.personium.core.PersoniumCoreException;
import io.personium.core.auth.AccessContext;
import io.personium.core.auth.OAuth2Helper.AcceptableAuthScheme;
import io.personium.core.auth.Privilege;

/**
 * JaxRS Resource オブジェクトから処理の委譲を受けてDav関連の永続化を除く処理を行うクラス.
 */
public class CellRsCmp extends DavRsCmp {

    Cell cell;
    AccessContext accessContext;
    String requestKey;
    String eventId;
    String ruleChain;
    String via;

    /**
     * Constructor.
     * @param davCmp DavCmp
     * @param cell Cell
     * @param accessContext AccessContext
     */
    public CellRsCmp(final DavCmp davCmp, final Cell cell, final AccessContext accessContext) {
        this(davCmp, cell, accessContext, null, null, null, null);
    }

    /**
     * Constructor.
     * @param davCmp DavCmp
     * @param cell Cell
     * @param accessContext AccessContext
     * @param requestKey X-Personium-RequestKey Header
     * @param eventId X-Personium-EventId Header
     * @param ruleChain X-Personium-RuleChain Header
     * @param via X-Personium-Via Header
     */
    public CellRsCmp(final DavCmp davCmp, final Cell cell, final AccessContext accessContext,
            final String requestKey, final String eventId, final String ruleChain, final String via) {
        super(null, davCmp);
        this.cell = cell;
        this.accessContext = accessContext;
        this.requestKey = requestKey;
        this.eventId = eventId;
        this.ruleChain = ruleChain;
        this.via = via;
    }

    /**
     * このリソースのURLを返します.
     * @return URL文字列
     */
    public String getUrl() {
        return this.cell.getUrl();
    }

    /**
     * リソースが所属するCellを返す.
     * @return Cellオブジェクト
     */
    public Cell getCell() {
        return this.cell;
    }

    /**
     * リソースが所属するBoxを返す.
     * @return Boxオブジェクト
     */
    public Box getBox() {
        return null;
    }

    /**
     * @return AccessContext
     */
    public AccessContext getAccessContext() {
        return this.accessContext;
    }

    /**
     * ACL情報を確認し、アクセス可能か判断する.
     * @param ac アクセスコンテキスト
     * @param privilege ACLのプリビレッジ（readとかwrite）
     * @return boolean
     */
    public boolean hasPrivilege(AccessContext ac, Privilege privilege) {

        // davCmpが無い（存在しないリソースが指定された）場合はそのリソースのACLチェック飛ばす
        if (this.davCmp != null
                && this.getAccessContext().requirePrivilege(this.davCmp.getAcl(), privilege, this.getCell().getUrl())) {
            return true;
        }
        return false;
    }

    /**
     * Performs access control.
     * @param ac Access context
     * @param privilege Required privilege
     */
    public void checkAccessContext(AccessContext ac, Privilege privilege) {
        // Check UnitUser token.
        if (ac.isUnitUserToken(privilege)) {
            return;
        }

        // Basic認証できるリソースかをチェック
        this.accessContext.updateBasicAuthenticationStateForResource(null);

        // アクセス権チェック
        if (!this.hasPrivilege(ac, privilege)) {
            // トークンの有効性チェック
            // トークンがINVALIDでもACL設定でPrivilegeがallに設定されているとアクセスを許可する必要があるのでこのタイミングでチェック
            if (AccessContext.TYPE_INVALID.equals(ac.getType())) {
                ac.throwInvalidTokenException(getAcceptableAuthScheme());
            } else if (AccessContext.TYPE_ANONYMOUS.equals(ac.getType())) {
                throw PersoniumCoreAuthzException.AUTHORIZATION_REQUIRED.realm(
                        ac.getRealm(), getAcceptableAuthScheme());
            }
            throw PersoniumCoreException.Auth.NECESSARY_PRIVILEGE_LACKING;
        }
    }

    /**
     * 認証に使用できるAuth Schemeを取得する.
     * @return 認証に使用できるAuth Scheme
     */
    @Override
    public AcceptableAuthScheme getAcceptableAuthScheme() {
        return AcceptableAuthScheme.BEARER;
    }

    /**
     * Get RequestKey.
     * @return RequestKey string
     */
    @Override
    public String getRequestKey() {
        return this.requestKey;
    }

    /**
     * Get EventId.
     * @return EventId string
     */
    @Override
    public String getEventId() {
        return this.eventId;
    }

    /**
     * Get RuleChain.
     * @return RuleChain string
     */
    @Override
    public String getRuleChain() {
        return this.ruleChain;
    }

    /**
     * Get Via.
     * @return Via string
     */
    @Override
    public String getVia() {
        return this.via;
    }

}
