import re

tabs = 0
isRec = False
def checkDigit(text):
    if (text == "0" or text =="1" or text =="2" or text =="3" or text =="4" or text =="5" or text =="6" or text =="7" or text =="8"
    or text =="9"):
        return True
    else:
        return False


def parseRegexp(text):

    regex = "(var |;|:?(\\s)?integer|:?(\\s)?real|:?(\\s)?boolean|\n)"

    reg = re.sub("(\t)", "", text)
    ch = " "
    mark = 0
    countSp = 0
    for c in text:
        countSp = countSp +1
        if c == ch:
            mark = countSp
        else:
            break
    reg = reg [mark:]
    reg = re.sub(regex, "", reg)
    reg = re.sub(":=", "=", reg)
    reg = re.sub("while ", "while (", reg)
    reg = re.sub("true", "True", reg)
    reg = re.sub("false", "False", reg)

    reg = re.sub("(?<=reverse\\s\\w\\.\\.\\w) ", ",-1 ",reg)
    reg = re.sub("reverse","", reg)
    reg = re.sub(" loop", ") :", reg)
    reg = re.sub("\\.\\.", ",", reg)
    reg = re.sub("in ", "in range (", reg)
    # reg = re.sub("if ", "if (", reg)
    #  reg = re.sub(" then", ") :", reg)
    reg = re.sub ("then", ":", reg)
    reg = re.sub("else", "else:", reg)
    reg = re.sub("routine", "def", reg)
    #reg = re.sub("(?<=[a-zA-Z])\\.", '["', reg)

    if (isRec == True):
        reg = re.sub(" is", '":', reg)
    else:
        reg = re.sub("is", "=", reg)

    words = reg.split()
    restored = ""

    for word in words:
        if "." in word:
            in1= (word[word.index(".")-1])
            if not checkDigit(in1):

                word = word.replace(".", '["')
                word = word+'"]'
                word = re.sub('\\)"]', '"])', word)
        if len(restored) == 0:
            restored = word
        else:
            restored = restored+" "+word

    if (len(restored) == 0):
        return reg
    else:
        return restored

def parseArray(line):

    if "array" in line and not "routine" in line:
        if (" = " in line):
            str = re.sub(":(\\s)*array", "", line)
            str = re.sub("\\[.*]", "", str)
        else:
            str = re.sub(":(\\s)*array", " =", line)
            str = re.sub("(\\[)", " ", str)
            str = re.sub("(])", " ", str)
            str = str+"*[None]"
        return str
    return None

def parseRecord (line):
    if "record" in line and not "routine" in line:
        str = re.sub("record", "{", line)
        if ":" in str:
            str = re.sub(":", "=", str)
        return str
    return None


def parseLine(line):

    pattern = re.compile("\\w+\\[\\w+\\](\\s)*=(\\s)*")
    if (pattern.match(line)):
        return line

    pattern = re.compile("\\w+(\\s)*=(\\s)*")
    if (pattern.match(line)):
        return line

    pattern = re.compile("(\\w)")
    if (pattern.match(line) and not "def" in line and not "end" in line
            and not "if" in line and not "for" in line and not "while" in line):
        if (not ":" in line and not "=" in line):
            if isRec:
                return '"'+line + '" : None'
            return line + " = None"
        else:
            if isRec:
                return '"'+line
            return line

    return None

def parseExpr(line):
    if ("*" in line or "/" in line or "%" in line or "+" in line or "-" in line or "and" in line or "or" in line or "xor" in line):
        return line
    return None

def parseRoutine(line):

    if "def" in line:
        if not "=" in line:
            return None
        str = line
        if ("array" in str):
            str = re.sub(":array", "", line)
            str = re.sub("\\[.*]", "", str)
        str = re.sub("=", ":", str)
        if not "(" in str:
            str = re.sub(":", "() :", str)
        return str


    return None

def parseEnd(line):

    if "end" in line:
        return True
    return None

def parseIf(line):
    if "if" in line:
        return line

    return None

def parseFor(line):
    if "for" in line:
        return line

    return None

def parseWhile(line):
    if "while" in line:
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




lines = []
curLine = 0
with (open("code.i")) as code:
    lines = code.readlines()

procLines = list()

# def parseLines():
#     global curLine
#     global tabs
#     global stringToWrite
#     a = lines[curLine]
#     if (a=="\n"):
#         curLine = curLine + 1
#         parseLines()
#     a = parseRegexp(a)
#     strp = parseLine(a)
#     if (strp is None):
#         strp = parseRoutine(a)
#         if (strp is None):
#             return " "
#         curLine = curLine + 1
#         stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
#         tabs = tabs + 1
#         parseBody()
#     else:
#         stringToWrite = stringToWrite + "\n"+"\t" * tabs + strp
#         curLine = curLine + 1
#         parseLines()


def parsing():
    global lines
    global stringToWrite
    global procLines
    global tabs
    global isRec
    for line in lines:
        a = parseRegexp(line)
        if (a!="\n"):

            if (parseRoutine(a) is not None):
                strp = parseRoutine(a)
                procLines.append("\t" * tabs + strp)
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
                tabs = tabs + 1
            elif (parseArray(a) is not None):
                strp = parseArray(a)
                procLines.append("\t" * tabs + strp)
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
            elif (parseRecord(a) is not None):
                strp = parseRecord(a)
                procLines.append("\t" * tabs + strp)
                isRec = True
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
            elif (parseIf(a) is not None):
                strp = parseIf(a)
                procLines.append("\t" * tabs + strp)
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
                tabs = tabs + 1
                ifScope = True
            elif (parseElse(a) is not None):
                strp = parseElse(a)
                tabs = tabs - 1
                procLines.append("\t" * tabs + strp)
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
                tabs = tabs +1
            elif (parseFor(a) is not None):
                strp = parseFor(a)
                procLines.append("\t" * tabs + strp)
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
                tabs = tabs + 1
                ifScope = True
            elif (parseWhile(a) is not None):
                strp = parseWhile(a)
                procLines.append("\t" * tabs + strp)
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
                tabs = tabs + 1
                ifScope = True
            elif (parseEnd(a) is not None):
                if (isRec):
                    procLines[len(procLines) - 1] = re.sub(",", "",procLines[len(procLines) - 1])
                    procLines.append("\t"*tabs+"}")
                    isRec = False
                else:
                    tabs = tabs - 1
                    if (tabs == 0):
                        ind = (procLines[len(procLines)-1]).index(" ")
                        procLines.append("\t"*(tabs+1)+"return"+procLines[len(procLines)-1][0:ind])
                    #procLines[len(procLines)-1] = "\t"*(tabs+1)+"return"+procLines[len(procLines)-1]

            elif (parseLine(a) is not None):
                strp = parseLine(a)
                if isRec:
                    procLines.append("\t" * tabs + strp+",")
                else:
                    procLines.append("\t" * tabs + strp)
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
            elif (parseExpr(a) is not None):
                strp = parseExpr(a)
                procLines.append("\t" * tabs + strp)
                stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp



parsing()
# for line in lines:
#     a = parseRegexp(line)
#     strp = parseLine(a)
#     if (strp is None):
#         strp = parseRoutine(a)
#         if (strp is None):
#             strp = parseEnd(a)
#             if (strp is None):
#                 pass
#             else:
#                 tabs = tabs - 1
#         else:
#             stringToWrite = stringToWrite + "\n" + "\t" * tabs + strp
#             tabs = tabs+1
#
#     else:
#         stringToWrite = stringToWrite + "\n"+"\t" * tabs + strp

for elem in procLines:
    print(elem)


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

