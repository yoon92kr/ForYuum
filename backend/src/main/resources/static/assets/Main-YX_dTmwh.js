import{p,m as w,a as C,g as k,u as q,b as D,c as r,d as y,e as j,f as E,h as P,_ as A,o as u,i as B,w as i,V as Z,j as d,r as _,F as b,k as f,l as S,t as v,n as K,q as z}from"./index-WXsVc_Oc.js";const J="data:image/svg+xml,%3csvg%20width='488'%20height='424'%20viewBox='0%200%20488%20424'%20fill='none'%20xmlns='http://www.w3.org/2000/svg'%3e%3cpath%20d='M249.126%2095.017L151.843%20263.694L243.959%20423.473L365.966%20211.973L487.918%200.473206H303.629L249.126%2095.017Z'%20fill='%231697F6'/%3e%3cpath%20d='M122.007%20211.973L128.396%20223.096L219.402%2065.2635L256.793%200.473206H243.959H0L122.007%20211.973Z'%20fill='%23AEDDFF'/%3e%3cpath%20d='M303.629%200.473206C349.743%20152.355%20243.959%20423.473%20243.959%20423.473L151.843%20263.694L303.629%200.473206Z'%20fill='%231867C0'/%3e%3cpath%20d='M256.793%200.473206C62.5042%200.473206%20128.397%20223.096%20128.397%20223.096L256.793%200.473206Z'%20fill='%237BC6FF'/%3e%3c/svg%3e",Q=p({fluid:{type:Boolean,default:!1},...w(),...C()},"v-container"),Y=k()({name:"VContainer",props:Q(),setup(e,n){let{slots:t}=n;const{rtlClasses:a}=q();return D(()=>r(e.tag,{class:["v-container",{"v-container--fluid":e.fluid},a.value,e.class],style:e.style},t)),{}}}),F=y.reduce((e,n)=>(e[n]={type:[Boolean,String,Number],default:!1},e),{}),M=y.reduce((e,n)=>{const t="offset"+j(n);return e[t]={type:[String,Number],default:null},e},{}),H=y.reduce((e,n)=>{const t="order"+j(n);return e[t]={type:[String,Number],default:null},e},{}),$={col:Object.keys(F),offset:Object.keys(M),order:Object.keys(H)};function X(e,n,t){let a=e;if(!(t==null||t===!1)){if(n){const s=n.replace(e,"");a+=`-${s}`}return e==="col"&&(a="v-"+a),e==="col"&&(t===""||t===!0)||(a+=`-${t}`),a.toLowerCase()}}const ee=["auto","start","end","center","baseline","stretch"],te=p({cols:{type:[Boolean,String,Number],default:!1},...F,offset:{type:[String,Number],default:null},...M,order:{type:[String,Number],default:null},...H,alignSelf:{type:String,default:null,validator:e=>ee.includes(e)},...w(),...C()},"v-col"),h=k()({name:"VCol",props:te(),setup(e,n){let{slots:t}=n;const a=E(()=>{const s=[];let c;for(c in $)$[c].forEach(l=>{const m=e[l],V=X(c,l,m);V&&s.push(V)});const o=s.some(l=>l.startsWith("v-col-"));return s.push({"v-col":!o||!e.cols,[`v-col-${e.cols}`]:e.cols,[`offset-${e.offset}`]:e.offset,[`order-${e.order}`]:e.order,[`align-self-${e.alignSelf}`]:e.alignSelf}),s});return()=>{var s;return P(e.tag,{class:[a.value,e.class],style:e.style},(s=t.default)==null?void 0:s.call(t))}}}),L=["start","end","center"],I=["space-between","space-around","space-evenly"];function x(e,n){return y.reduce((t,a)=>{const s=e+j(a);return t[s]=n(),t},{})}const se=[...L,"baseline","stretch"],O=e=>se.includes(e),R=x("align",()=>({type:String,default:null,validator:O})),ne=[...L,...I],T=e=>ne.includes(e),W=x("justify",()=>({type:String,default:null,validator:T})),ae=[...L,...I,"stretch"],G=e=>ae.includes(e),U=x("alignContent",()=>({type:String,default:null,validator:G})),N={align:Object.keys(R),justify:Object.keys(W),alignContent:Object.keys(U)},oe={align:"align",justify:"justify",alignContent:"align-content"};function le(e,n,t){let a=oe[e];if(t!=null){if(n){const s=n.replace(e,"");a+=`-${s}`}return a+=`-${t}`,a.toLowerCase()}}const re=p({dense:Boolean,noGutters:Boolean,align:{type:String,default:null,validator:O},...R,justify:{type:String,default:null,validator:T},...W,alignContent:{type:String,default:null,validator:G},...U,...w(),...C()},"v-row"),g=k()({name:"VRow",props:re(),setup(e,n){let{slots:t}=n;const a=E(()=>{const s=[];let c;for(c in N)N[c].forEach(o=>{const l=e[o],m=le(c,o,l);m&&s.push(m)});return s.push({"v-row--no-gutters":e.noGutters,"v-row--dense":e.dense,[`align-${e.align}`]:e.align,[`justify-${e.justify}`]:e.justify,[`align-content-${e.alignContent}`]:e.alignContent}),s});return()=>{var s;return P(e.tag,{class:["v-row",a.value,e.class],style:e.style},(s=t.default)==null?void 0:s.call(t))}}}),ie={name:"HelloWorld",data:()=>({ecosystem:[{text:"vuetify-loader",href:"https://github.com/vuetifyjs/vuetify-loader/tree/next"},{text:"github",href:"https://github.com/vuetifyjs/vuetify/tree/next"},{text:"awesome-vuetify",href:"https://github.com/vuetifyjs/awesome-vuetify"}],importantLinks:[{text:"Chat",href:"https://community.vuetifyjs.com"},{text:"Made with Vuetify",href:"https://madewithvuejs.com/vuetify"},{text:"Twitter",href:"https://twitter.com/vuetifyjs"},{text:"Articles",href:"https://medium.com/vuetify"}],logo:J,whatsNext:[{text:"Explore components",href:"https://vuetifyjs.com"},{text:"Roadmap",href:"https://vuetifyjs.com/introduction/roadmap/"},{text:"Frequently Asked Questions",href:"https://vuetifyjs.com/getting-started/frequently-asked-questions"}]})},ce=f("h1",{class:"display-2 font-weight-bold mb-3"}," Welcome to the Vuetify 3 Beta ",-1),ue=f("h4",null,"Vite Preview",-1),fe=f("p",{class:"subheading font-weight-regular"},[S(" For help and collaboration with other Vuetify developers, "),f("br"),S("please join our online "),f("a",{href:"https://community.vuetifyjs.com",target:"_blank"},"Discord Community")],-1),de=f("h2",{class:"headline font-weight-bold mb-5"}," What's next? ",-1),he=["href"],me=f("h2",{class:"headline font-weight-bold mb-5"}," Important Links ",-1),ge=["href"],ye=f("h2",{class:"headline font-weight-bold mb-5"}," Ecosystem ",-1),_e=["href"];function be(e,n,t,a,s,c){return u(),B(Y,null,{default:i(()=>[r(g,{class:"text-center"},{default:i(()=>[r(h,{cols:"12"},{default:i(()=>[r(Z,{src:e.logo,class:"my-3",contain:"",height:"200"},null,8,["src"])]),_:1}),r(h,{class:"mb-4"},{default:i(()=>[ce,ue,fe]),_:1}),r(h,{class:"mb-5",cols:"12"},{default:i(()=>[de,r(g,{justify:"center"},{default:i(()=>[(u(!0),d(b,null,_(e.whatsNext,(o,l)=>(u(),d("a",{key:l,href:o.href,class:"subheading mx-3",target:"_blank"},v(o.text),9,he))),128))]),_:1})]),_:1}),r(h,{class:"mb-5",cols:"12"},{default:i(()=>[me,r(g,{justify:"center"},{default:i(()=>[(u(!0),d(b,null,_(e.importantLinks,(o,l)=>(u(),d("a",{key:l,href:o.href,class:"subheading mx-3",target:"_blank"},v(o.text),9,ge))),128))]),_:1})]),_:1}),r(h,{class:"mb-5",cols:"12"},{default:i(()=>[ye,r(g,{justify:"center"},{default:i(()=>[(u(!0),d(b,null,_(e.ecosystem,(o,l)=>(u(),d("a",{key:l,href:o.href,class:"subheading mx-3",target:"_blank"},v(o.text),9,_e))),128))]),_:1})]),_:1})]),_:1})]),_:1})}const ve=A(ie,[["render",be]]),pe=K({name:"HomeView",components:{HelloWorld:ve}});function we(e,n,t,a,s,c){const o=z("HelloWorld");return u(),B(o)}const ke=A(pe,[["render",we]]);export{ke as default};