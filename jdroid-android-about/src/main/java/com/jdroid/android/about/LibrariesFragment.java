package com.jdroid.android.about;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.ReflectionUtils;

import java.util.List;

public class LibrariesFragment extends AbstractRecyclerFragment {
	
	private List<Library> libraries = Lists.newArrayList();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		libraries.add(new Library("jdroid", "Jdroid Framework", "Maxi Rosson", "http://jdroidframework.com"));
		if (ReflectionUtils.getSafeClass("com.jdroid.android.uil.UilAppModule") != null) {
			Library library = new Library("universalImageLoader", "Universal Image Loader",
					"Sergey Tarasevich", "https://github.com/nostra13/Android-Universal-Image-Loader");
			libraries.add(library);
		}
		libraries.addAll(getCustomLibraries());
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setAdapter(new RecyclerViewAdapter(new LibraryRecyclerViewType(), libraries));
	}
	
	protected List<Library> getCustomLibraries() {
		return Lists.newArrayList();
	}

	public class LibraryRecyclerViewType extends RecyclerViewType<Library, LibrariesHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_library_item;
		}

		@Override
		protected Class<Library> getItemClass() {
			return Library.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			LibrariesHolder holder = new LibrariesHolder(view);
			holder.name = findView(view, R.id.name);
			holder.author = findView(view, R.id.author);
			return holder;
		}

		@Override
		public void fillHolderFromItem(Library item, LibrariesHolder holder) {
			holder.name.setText(item.getName());
			holder.author.setText(item.getAuthor());
		}

		@Override
		public void onItemSelected(Library item, View view) {
			item.onSelected(getActivity());
			AboutAppModule.get().getModuleAnalyticsSender().trackAboutLibraryOpen(item.getLibraryKey());
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return LibrariesFragment.this;
		}
	}

	public static class LibrariesHolder extends RecyclerView.ViewHolder {

		protected TextView name;
		protected TextView author;

		public LibrariesHolder(View itemView) {
			super(itemView);
		}
	}
	
}
