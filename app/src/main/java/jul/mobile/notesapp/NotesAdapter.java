package jul.mobile.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter {

    private Context context;
    private List<KontenNotes> notesList;

    public NotesAdapter(Context context) {
        this.context = context;
        this.notesList = new ArrayList<>();
    }

    public void setData(List<KontenNotes> notesList) {
        this.notesList = notesList;
    }
}
