import re

tabs = 0

def parseRegexp(text):

    regex = "(var |;|:(\\s)?integer|:(\\s)?real|:(\\s)?boolean|\n)"

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
    reg = re.sub("is", "=", reg)



    return reg

def parseLine(line):

    pattern = re.compile("\\w(\\s)*=(\\s)*\\d+")
    if (pattern.match(line)):
        return line

    pattern = re.compile("(\\w)")
    if (pattern.match(line) and not "def" in line and not "end" in line):

        return line + " = None"

    return None

def parseRoutine(line):

    if "def" in line:
        if not "=" in line:
            return None
        return re.sub("=", ":", line)

    return None

def parseEnd(line):

    if "end" in line:
        return True
    return None

def parseIf(line):
    if "if" in line:
        return line

    return None

def parseElse(line):
    if "else" in line:
        return line

    return None

current_fun = parseLine

stringToWrite = ""
# print(parseRegexp("routine main is for i in reverse 4..5 loop if true then kk else end"))
a = parseRegexp("")

with open("code.i") as code:
    for line in code:
        a = parseRegexp(line)
        strp = parseLine(a)
        if (strp is None):
            strp = parseRoutine(a)
            if (strp is None):
                strp = parseEnd(a)
                if (strp is None):
                    pass
                else:
                    tabs = tabs - 1
            else:
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
                tabs = tabs+1

        else:
            stringToWrite = stringToWrite + "\n"+"\t" * tabs + strp

print(stringToWrite)


#
# while (True):
#     strp = parseLine(a)
#     if (strp is None):
#         strp = parseRoutine(a)
#         if (strp is None):
#             pass
#         else:
#             parseBody()
#     else:
#         stringToWrite = stringToWrite+"\t"*tabs+strp
#     print(strp)



