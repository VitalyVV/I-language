import re

def parseRegexp(text):

    regex = "(var |;)"

    reg = re.sub(regex, "", text)
    reg = re.sub(":=", "=", reg)
    reg = re.sub("while ", "while (", reg)
    reg = re.sub("(?<=reverse\\s\\w\\.\\.\\w) ", ",-1 ",reg)
    reg = re.sub("reverse","", reg)
    reg = re.sub(" loop", ") :", reg)
    reg = re.sub("\\.\\.", ",", reg)
    reg = re.sub("in ", "in range (", reg)
    reg = re.sub("if ", "if (", reg)
    reg = re.sub(" then", ") :", reg)
    reg = re.sub("else", "else:", reg)
    reg = re.sub("routine", "def", reg)


    return reg


# print(parseRegexp("routine main is for i in reverse 4..5 loop if true then kk else end"))
print(parseRegexp("var a: integer is 5 var b: integer is 5 routine main() is var n is a + b for i in reverse 4..5 loop end"))

