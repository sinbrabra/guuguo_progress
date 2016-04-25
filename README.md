# guuguo\_progress
## first progress
![][image-1] 
   <top.guuguo.progress_lib.view.MyProgressWave
                    android:layout_width="150dp"
                    android:layout_height="150dp"
                    android:layout_gravity="center"
                    item:wave_circle_width="10dp"
                    item:wave_enable="false"
                    item:wave_enable_center_text="true"
                    item:wave_finished_color="@color/colorAccent"
                    item:wave_max="100"
                    item:wave_prefix_text="进度:"
                    item:wave_progress="30"
                    item:wave_style="square"
                    item:wave_suffix_text="%"
                    item:wave_text_color="#FFFFFF"
                    item:wave_unfinished_color="@color/colorPrimary"
                    />

                <top.guuguo.progress_lib.view.MyProgressWave
                    android:layout_width="150dp"
                    android:layout_height="150dp"
                    android:layout_gravity="center"
                    android:layout_marginLeft="20dp"
                    item:wave_circle_width="2dp"
                    item:wave_enable_center_text="true"
                    item:wave_finished_color="@color/colorAccent"
                    item:wave_force_title="强制文字"
                    item:wave_max="100"
                    item:wave_progress="50"
                    item:wave_text_color="@color/colorPrimaryDark"
                    item:wave_unfinished_color="#ffffff"
                    />

            <top.guuguo.progress_lib.view.MyProgressRoundBarView
                android:layout_width="200dp"
                android:layout_height="200dp"
                android:layout_gravity="center"
                item:round_bar_center_text="哈哈"
                item:round_bar_center_text_color="@color/colorAccent"
                item:round_bar_finished_color="@color/colorAccent"
                item:round_bar_progress="30"
                item:round_bar_progress_max="100"
                item:round_bar_unfinished_color="@color/colorPrimary"
                item:round_bar_width="10dp"
                />
![][image-2] 
            <top.guuguo.progress_lib.view.MyProgressInstrument
                android:layout_width="200dp"
                android:layout_height="200dp"
                android:layout_gravity="center"
                item:Instrument_background_color="@color/colorPrimaryDark"
                item:Instrument_finished_color="#44000000"
                item:Instrument_progress="8"
                item:Instrument_progress_max="10"

                />

            <top.guuguo.progress_lib.view.MyProgressHalfCircleBarView
                android:layout_width="300dp"
                android:layout_height="150dp"
                android:layout_gravity="center"
                item:half_background_color="@color/colorAccent"
                item:half_center_text="5"
                item:half_finished_color="@color/colorAccent"
                item:half_progress="30"
                item:half_progress_max="100"
                item:half_unfinished_color="@color/colorPrimary"
                item:half_width="20dp"
                />

[image-1]:	preview/1.gif
[image-2]:	preview/2.jpg