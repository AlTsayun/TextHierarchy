package fx;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileDialogConstructorParam {
    public final FileDialogListener listener;
    public final boolean isSaving;
}
