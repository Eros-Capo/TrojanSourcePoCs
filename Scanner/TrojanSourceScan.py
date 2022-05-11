import platform, os, glob, argparse, sys
import TSUnicode

TSU_PATH=os.path.abspath(TSUnicode.__file__) # to exclude the Unicode list from the control
BOLD_START=BOLD_END=''
if (platform.system()!='Windows'):
    BOLD_START='\033[1m'
    BOLD_END='\033[0m'
NO_FILE_FOUND='No file found'
NO_TS_FOUND='No Trojan Source detected'

parser=argparse.ArgumentParser(description='Software for source code analysis, looking for Trojan Source vulnerabilities.\nPathnames can be written in Unix style. In Windows, use directories, \"**\" or \"**/*\" for subdirectories recursive inspection (when -r is flagged), in Linux \"*\" is sufficient.\nOnly text files are accepted.\nExample:\n\t'+os.path.basename(__file__)+' -rf ./ -o output.txt --verbose\n')
parser.add_argument('paths', metavar='F', type=str, nargs='+', help='List of paths to be analyzed.')
parser.add_argument('-r', '--recursive', action='store_true', help='Recursive search, in Windows use [DIRPATH]/**/* instead of [DIRPATH]/*, or similarly [DIRPATH]/**/*.py, etc.')
parser.add_argument('-o', '--output', action='store', help='Output file for logging.')
parser.add_argument('-v', '--verbose', action='store_true', help='Prints more information about files not correctly decoded.')
parser.add_argument('-f', '--faster', action='store_true', help='Performs a faster control, using a reduced homoglyphs list.')

homoglyphs=TSUnicode.homoglyphs
if parser.parse_args().faster:
    homoglyphs=TSUnicode.homoglyphs_f

def findTrojanSource(filename, verbose=False):
    with open(filename, encoding='utf-8') as file:
        to_print=line_to_print=tstype=''
        found_in_line=found_in_file=found_bidi=found_homoglyph=found_eol=False
        lineno=0
        try:
            lines=file.readlines()
        except UnicodeDecodeError:
            encode_err=''
            if verbose:
                encode_err='Decode error for file: '+filename+'\nMake sure all files only contain text\n\n\n\n'
            return False, encode_err
        for line in lines:
            line_to_print=line
            lineno+=1
            tstype=''
            for b in TSUnicode.bidi:
                if b in line_to_print:
                    line_to_print=line_to_print.replace(b, BOLD_START+'{U+'+format(ord(b), '04x').upper()+'}'+BOLD_END)
                    found_in_line=found_in_file=found_bidi = True
            for h in homoglyphs:
                if h in line_to_print:
                    line_to_print=line_to_print.replace(h, BOLD_START+'{U+'+format(ord(h), '04x').upper()+'}'+BOLD_END)
                    found_in_line=found_in_file=found_homoglyph = True
            for e in TSUnicode.eol:
                if e in line_to_print:
                    line_to_print=line_to_print.replace(e, BOLD_START+'{'+TSUnicode.eol[e]+'}'+BOLD_END)
                    found_in_line=found_in_file=found_eol= True                
            if found_bidi:
                tstype+='Bidi'
            if found_homoglyph:
                if found_bidi:
                    tstype+=', ' if found_eol else ' and '
                tstype+='Homoglyph'
            if found_eol:
                if found_bidi or found_homoglyph:
                    tstype+=' and '
                tstype+='New Line Escape'
            if (found_in_line):
                to_print+='Suspected Trojan Source ('+tstype+'): file \''+filename+'\', line '+str(lineno)+'\n\t'+line_to_print+'\n'
            line_to_print=''
            found_in_line=found_bidi=found_homoglyph=found_eol=False
    if found_in_file:
        to_print+='\n'
    return found_in_file, to_print

args = parser.parse_args()
filePresent=TSFound=TSFound_in_file=False
TSresult=file_result=''

for arg in args.paths:
    for path in glob.glob(arg, recursive=args.recursive):
        abspath=os.path.abspath(path)
        if os.path.isdir(abspath):
            for dir, subdir, files in os.walk(arg):                
                for f in files:
                    if (os.path.abspath(dir) == abspath or args.recursive) and not f.endswith('.lnk') and os.path.abspath(dir+'/'+f)!=TSU_PATH:
                        TSFound_in_file, file_result=findTrojanSource(os.path.abspath(dir+'/'+f), args.verbose)
                        TSresult+=file_result
                        TSFound = TSFound or TSFound_in_file
                        filePresent=True
        elif not os.path.islink(abspath) and not abspath.endswith('.lnk') and abspath!=TSU_PATH:
            TSFound_in_file, file_result=findTrojanSource(abspath, args.verbose)
            TSresult+=file_result
            TSFound = TSFound or TSFound_in_file
            filePresent=True

if not filePresent:
    TSresult+=NO_FILE_FOUND
elif not TSFound:
    TSresult+=NO_TS_FOUND

out=sys.stdout
if (TSFound):
    out=sys.stderr

log_write=''
for ch in TSresult:
    try:
        print(ch, file=out, end='')
        log_write+=ch
    except UnicodeEncodeError:
        continue

if args.output!=None:
    with open(args.output, 'w') as log:
        log.write(log_write.replace(BOLD_START, '').replace(BOLD_END, ''))