import _ from"./AccountLogin-d5c265c4.js";import{o as m,C as d,q as a,t as b,M as f,v as o,w as r,a0 as g}from"./vendor-7d8626b5.js";import"./index-16b2bfc2.js";import"./useSmsCode-cb2741cc.js";import"./keepAlive-00d3a930.js";/* empty css                                                                    */const B={class:"login-block tabs-wrap"},h=m({__name:"index",emits:["forget","register","appoint"],setup(C,{emit:e}){const t=d("account"),s=()=>{e("forget")},l=()=>{e("register")},c=()=>{e("appoint")};return(k,n)=>{const p=a("el-tab-pane"),u=a("el-tabs");return b(),f("div",B,[o(u,{modelValue:t.value,"onUpdate:modelValue":n[0]||(n[0]=i=>t.value=i),stretch:""},{default:r(()=>[o(p,{label:"\u8D26\u53F7\u5BC6\u7801\u767B\u5F55",name:"account"},{default:r(()=>[o(_,{onForget:s,onRegister:l,onAppoint:c})]),_:1}),g("",!0)]),_:1},8,["modelValue"])])}}});export{h as _};