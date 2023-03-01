/*
 * Copyright (c) 2022-present, by Szymon Blachuta and Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package org.szb.ziarnakodu;

import org.szb.ziarnakodu.lex.ILEXTOKEN;

public enum JsonGineLEXTOKEN implements ILEXTOKEN{
	IDENT             , // A-Z, a-z, _, 0-9, $
	STRING           , // 'string' "string"       (literals)
	NUMBER           , // 0 123 1.2 1.2e+2        (literals)
	OPERATOR         , // + - * ** / // % << >> & | ^ ~ < > <= >= == != = += -= *=  /= //= %= &= |= ^= >>= <<= **=
	PROPERTY         , // #Abc
	//DELIMITER        , // ( ) [ ] { } , : . ; @ -> ...
	COMMENT          , // /*...*/ | //
    COMMENT_EOL      , // //...
    COMMENT_INL      , // /*comment*/ 
    WS               , // white space
    ENDMARKER        , // end of file
	;
}
