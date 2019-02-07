(ns select-nrepl.core-test
  (:require
   [midje.sweet :refer :all]
   [select-nrepl.test-helpers :refer :all]))

(facts "about selecting elements"
  (tabular
    (select "whole" "element" ?input) => ?output
    ?input             ?output
    "<hello>"          "<hello>"
    "  hello/<>world"  "  <hello/world>"
    " :foo/ba<>r"      " <:foo/bar>"
    " \\new<>line"     " <\\newline>"
    " \"a s<t>ring\" " " <\"a string\"> "
    " 4200<>0  x"      " <42000>  x"
    "++ #\"<>x\" ;;-"  "++ <#\"x\"> ;;-"
    " #fo<>o \"h\" "   " <#foo \"h\"> "
    " \"h<>ello\nw\" " " <\"hello\nw\"> "
    "a ^S foo<>b y"    "a <^S foob> y"
    "a #^S foo<>b y"   "a <#^S foob> y"

    "( he<>llo there)" "( <hello> there)"
    "h <>"             "h "
    "<> h ello"        " <h> ello"
    "h <> (ello t)"    "h  (<ello> t)"
    "h <> ([ello t])"  "h  ([<ello> t])")

  (tabular
    (select "inside" "element" ?input) => ?output
    ?input ?output
    " \"a s<t>ring\" "  " \"<a string>\" "
    " #\"a <>regex\" x" " #\"<a regex>\" x"
    " #fo<>o \"hi\" "   " #foo \"<hi>\" "
    " \"he\nt<>here \"" " \"<he\nthere >\""))

(facts "about selecting forms"
  (tabular
    (select "whole" "form" ?input) => ?output
    ?input                ?output
    "x (he<>l wo) 4"      "x <(hel wo)> 4"
    "x [he<>l wo] 4"      "x <[hel wo]> 4"
    "x {he<>l wo} 4"      "x <{hel wo}> 4"
    "x #{he<>l wo} 4"     "x <#{hel wo}> 4"
    "x #:foo{:b<>ar 4} 4" "x <#:foo{:bar 4}> 4"
    "x `(foo ~b<>ar) 4"   "x <`(foo ~bar)> 4"
    "x ~(foo ~b<>ar) 4"   "x <~(foo ~bar)> 4"
    "x ~@(foo ~b<>ar) 4"  "x <~@(foo ~bar)> 4"
    "x ^S {he<>l wo} 4"   "x <^S {hel wo}> 4"
    "x #^S {he<>l wo} 4"  "x <#^S {hel wo}> 4"
    "x '(he<>l wo) 4"     "x <'(hel wo)> 4"
    "x #f/b (he<>l wo) 4" "x <#f/b (hel wo)> 4"

    "x (he (ll<>o wo) l)" "x (he <(llo wo)> l)"
    "x (h<>e (llo wo) l)" "x <(he (llo wo) l)>")
  (tabular
    (select "inside" "form" ?input) => ?output
    ?input                ?output
    "x (he<>l wo) 4"      "x (<hel wo>) 4"
    "x [he<>l wo] 4"      "x [<hel wo>] 4"
    "x {he<>l wo} 4"      "x {<hel wo>} 4"
    "x #{he<>l wo} 4"     "x #{<hel wo>} 4"
    "x #:foo{:b<>ar 4} 4" "x #:foo{<:bar 4>} 4"
    "x `(foo ~b<>ar) 4"   "x `(<foo ~bar>) 4"
    "x ~(foo ~b<>ar) 4"   "x ~(<foo ~bar>) 4"
    "x ~@(foo ~b<>ar) 4"  "x ~@(<foo ~bar>) 4"

    "x (he (ll<>o wo) l)" "x (he (<llo wo>) l)"
    "x (h<>e (llo wo) l)" "x (<he (llo wo) l>)"))
