import os
import shutil

if os.path.isdir('testpages_'):
    shutil.rmtree('testpages_')

os.mkdir('testpages_')

for path, dirs, files  in os.walk('testpages'):
    if len(files) != 0:
        for f in files:
            if not f.startswith('.'):
                full_path = path + '/' + f
                names = full_path.split('/')
                out = [x[0:5] for x in names[1:-2]]
                new_full_path = names[0] + '_' + '/' + names[-2] + '_' +'_'.join(out) + '_' + names[-1]

                shutil.copy(full_path, new_full_path)
