import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/login/theme/lumo/vaadin-login-form.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/scroller/theme/lumo/vaadin-scroller.js';
import '@vaadin/side-nav/theme/lumo/vaadin-side-nav.js';
import '@vaadin/side-nav/theme/lumo/vaadin-side-nav-item.js';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/icon/theme/lumo/vaadin-icon.js';
import '@vaadin/avatar/theme/lumo/vaadin-avatar.js';
import 'Frontend/generated/jar-resources/menubarConnector.js';
import '@vaadin/menu-bar/theme/lumo/vaadin-menu-bar.js';
import '@vaadin/context-menu/theme/lumo/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '6e1993be745df908a5d37a475a368faf4d827ac7c84bc56f07952240a718252f') {
    pending.push(import('./chunks/chunk-15af0249255aff77b08a437f8b45bdfe2d0f6ae1b589ff6e1bc8afec857a28ca.js'));
  }
  if (key === '21b7d74bb585219623a3a26866fa6265660c5c006f9b04663ccacfa38bb4a44e') {
    pending.push(import('./chunks/chunk-b8a0f2deb6375787850e04a4e8edc82fd42c55915e107044d48721f0af5a0147.js'));
  }
  if (key === 'd870ccddf331bfd85a9bb977b732831f9b0b0a5846800907493a042f2af3d74c') {
    pending.push(import('./chunks/chunk-f830842efd895708388c946d7fa15f31e9ff59f3eb5b8d1054bc976f0aa5762f.js'));
  }
  if (key === '3856322ae8de4b97cf0721546a9ab08b6846415756c9032707eea5dff1ed3f7d') {
    pending.push(import('./chunks/chunk-f830842efd895708388c946d7fa15f31e9ff59f3eb5b8d1054bc976f0aa5762f.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}