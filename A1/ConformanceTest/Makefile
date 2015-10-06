# Neil Edelman -- Makefile 1.2 (GNU Make 3.81)
# make:				make the programme MAIN, with src/ to bin/
# make test:			junit tests; tests are assumed to be in a
#				parallel directory, test/
# make run:			run the java programme
# make clean:			delete bin/
# make backup [<description>]:	add a snapshot zip to backup; src/, test/, and
#				EXTRA

# info
PROJ  := ConformanceTest
VA    := 1
VB    := 0

# dirs
SDIR  := src
BDIR  := bin
TDIR  := test
LDIR  := lib
BACK  := backup

# where the main() is
MAIN  := ca/mcgill/ecse429/conformancetest/ccoinbox/CCoinBox
EXTRA := Makefile ConformanceTestReport.tex ConformanceTestReport.pdf

# auto-generate tests
NPLUS := ca/mcgill/ecse429/conformancetest/nplus/Nplus
XML = \
 ccoinbox.xml:ca/mcgill/ecse429/conformancetest/ccoinbox/CCoinBox.java \
 legislation.xml:ca/mcgill/ecse429/conformancetest/legislation/Legislation.java
NPLUSDEP := $(call rwildcard, $(SDIR)/ca/mcgill/ecse429/conformancetest/nplus/, *.java)

# set this in where the files hamcrest-core-1.3.jar, junit-4.12.jar can be found
JUNITHOME := /Users/neil/Music/java

# compiler options
CC   := javac
CF   := -g:none -O -Xlint:unchecked -Xlint:deprecation# -verbose

######
# derived strings

# $(SPACE) is ' '
EMPTY :=
SPACE := $(EMPTY) $(EMPTY)

# John Graham-Cumming:
# rwildcard is a recursive wildcard
rwildcard=$(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2) $(filter $(subst *,%,$2),$d))

# Jakob Borg and Eldar Abusalimov:
# $(ARGS) is all the extra arguments
# $(BRGS) is_all_the_extra_arguments
ifeq (backup, $(firstword $(MAKECMDGOALS)))
  ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  BRGS := $(subst $(SPACE),_,$(ARGS))
  ifneq (,$(BRGS))
    BRGS := -$(BRGS)
  endif
  $(eval $(ARGS):;@:)
endif

JAVA  := $(call rwildcard, $(SDIR), *.java)
OBJ   := $(patsubst %,$(BDIR)/%.class,$(MAIN))
SRC   := $(patsubst %,$(SDIR)/%.java,$(MAIN))
OBJN  := $(patsubst %,$(BDIR)/%.class,$(NPLUS))
SRCN  := $(patsubst %,$(SDIR)/%.java,$(NPLUS))
LIB   := $(BDIR):$(subst $(SPACE),:,$(wildcard $(LDIR)/*.jar))

TEST  := $(call rwildcard, $(TDIR), *.java)
UTEST := $(subst /,.,$(patsubst $(TDIR)/%.java,%,$(TEST)))
ULIB := $(JUNITHOME)/hamcrest-core-1.3.jar:$(JUNITHOME)/junit-4.12.jar

INST  := $(PROJ)-$(VA)_$(VB)

######
# compiles the programme by default

default: $(OBJ)

nplus: statemodel $(OBJN) $(NPLUSDEP)

$(OBJ): $(JAVA)
	# making programme
	@mkdir -p $(BDIR)
	$(CC) $(CF) -d $(BDIR) -sourcepath $(SDIR) -cp $(LIB) $(SRC)

$(OBJN): $(JAVA)
	# making N+
	@mkdir -p $(BDIR)
	$(CC) $(CF) -d $(BDIR) -sourcepath $(SDIR) -cp $(LIB) $(SRCN)

######
# phoney targets

.PHONY: test run clean backup nplus autotest

statemodel: $(call rwildcard, $(SDIR)/ca/mcgill/ecse429/conformancetest/statemodel/, *.java)
	# making statemodel
	@mkdir -p $(BDIR)
	$(CC) $(CF) -d $(BDIR) -sourcepath $(SDIR) -cp $(LIB) $(SDIR)/ca/mcgill/ecse429/conformancetest/statemodel/persistence/PersistenceStateMachine.java

autotest: nplus
	# auto-test from xml
	@mkdir -p $(TDIR)
	@$(foreach xml,$(XML), \
		$(eval sxml := $(subst :,$(SPACE),$(xml))) \
		$(eval XMLFILE := $(word 1,$(sxml))) \
		$(eval XMLPATH := $(word 2,$(sxml))) \
		$(eval XMLNEW  := $(TDIR)/$(dir $(XMLPATH))GeneratedTest$(notdir $(XMLPATH))) \
		echo Make: java $(NPLUS) $(XMLFILE) \> $(XMLNEW); \
		java -cp $(LIB) $(NPLUS) $(XMLFILE) > $(XMLNEW); \
		$(CC) $(CF) -d $(BDIR) -sourcepath $(TDIR):$(SDIR) -cp $(ULIB):$(LIB) $(XMLNEW); \
	)

test: default nplus autotest
	# compile all tests
	@$(foreach test,$(TEST),echo Make: $(CC) $(test);$(CC) $(CF) -d $(BDIR) -sourcepath $(SDIR) -cp $(ULIB):$(LIB) $(test);)
	# run the tests
	@$(foreach utest,$(UTEST),echo Make: java $(utest);java -cp $(ULIB):$(LIB) org.junit.runner.JUnitCore $(utest) 2>/dev/null;)

run: default
	java -cp $(LIB) $(MAIN)

clean:
	# meh
	-rm -rf $(BDIR)

backup:
	@mkdir -p $(BACK)
	zip -r $(BACK)/$(INST)-`date +%Y-%m-%dT%H%M%S`$(BRGS).zip $(SDIR) $(TDIR) $(EXTRA) -x \*/.DS_Store
