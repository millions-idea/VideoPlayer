/***
 * @pName Admin
 * @name YaBoScriptUtil
 * @user HongWei
 * @date 2019/4/24
 * @desc
 */
package com.management.admin.utils.yabo;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class YaBoScriptUtil {
    private static final String JSCode = "'__cdn_clearance=1556091349.25|0|' + (function () {\n" +
            "        return [\n" +
            "            [{} + []][0].charAt(2), 'v', [~~!{}], 'GUP', (!{} + [] + []).charAt(-~-~(+[])) + (-~[-~[] - ~(-~((-~{} | -~-~(+[]))))] + [] + []) + [!/!/ + [] + []][0].charAt(-~{} + 2), 'RF', [\n" +
            "                [] / !/!/ + [] + [\n" +
            "                    []\n" +
            "                ][0]\n" +
            "            ][0].charAt((+[])) + ({} + [] + []).charAt(-~{} + 2), 'Sz', [-~[(-~![] << -~![])]] + [{} + []][0].charAt(2) + [{} + [\n" +
            "                []\n" +
            "            ][0]][0].charAt((-~{} + [] + []) + [-~[(-~![] << -~![])]]) + [-~[(-~![] << -~![])] + (-~![] << -~![]) + (-~-~(+[]) << -~(+[]))], 'H', [5] + [6], 'M', (-~[-~[] - ~(-~((-~{} | -~-~(+[]))))] + [] + []), 'm', ({} + [] + []).charAt(-~(-~((-~{} | -~-~(+[]))))), 'M%', [-~[(-~![] << -~![])]], 'D'\n" +
            "        ].join('')\n" +
            "    })()";


    public static Object evalJS(){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try{

            Object result = engine.eval(JSCode);
            return result;

            // engine.eval("alert(\"js alert\");");    // 不能调用浏览器中定义的js函数 // 错误，会抛出alert引用不存在的异常
        }catch(ScriptException e){

            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
        String str = (String)YaBoScriptUtil.evalJS();
        System.out.println(str);
    }
}

