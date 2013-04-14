package com.appjam.team16;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.appjam.team16.QuestionListFragment.OnQuestionSelectedListener;

public class ViewQuestionsActivity extends FragmentActivity implements OnQuestionSelectedListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_questions);
		
		//we're in single pane view
		//taken from the android exmaple
		if (findViewById(R.id.questionFragmentContainer) != null)
		{
			// However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            QuestionListFragment listFragment = new QuestionListFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            listFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.questionFragmentContainer, listFragment).commit();
		}
	}
	
	public void onQuestionSelected(long id) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        QuestionDetailFragment detailFrag = (QuestionDetailFragment)
        		getSupportFragmentManager().findFragmentById(R.id.questionDetailFragment);
        
        if (detailFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            detailFrag.displayQuestion(id);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...
        	QuestionDetailFragment detailFragment = new QuestionDetailFragment();
        	Bundle args = new Bundle();
        	args.putLong(QuestionDetailFragment.QUESTION_ID, id);
        	detailFragment.setArguments(args);
        	
        	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        	transaction.replace(R.id.questionFragmentContainer, detailFragment);
        	transaction.addToBackStack(null);
        	
        	transaction.commit();
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
